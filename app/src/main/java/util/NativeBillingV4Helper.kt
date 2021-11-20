package util

import billing.IabHelper
import billing.IabResult
import billing.Inventory
import billing.Purchase
import billing.SKU
import main.ApplicationClass
import ui.BaseActivity
import util.extension.isAppAvailable

class NativeBillingV4Helper(private val activity: BaseActivity, private val appClass: ApplicationClass) {

    val skuList = arrayListOf<SKU>()
    private var onInitSuccess: (ArrayList<SKU>) -> Unit = {}
    private var onInitFailure: (Boolean, String, String, Int, String) -> Unit = { needLogin, title, desc, code, extra -> }
    private var onPurchaseFailure: (Boolean, String, String, Int, String) -> Unit = { canceled, title, desc, code, extra -> }
    private var onPurchaseSuccess: (SKU) -> Unit = { }


    lateinit var iabHelper: IabHelper
    private var inventory: Inventory? = null
    var initialized = false

    companion object {
        var errorTrace = "0"
    }

    fun init(licenseKey: String, skuList: ArrayList<SKU>, onSuccess: (ArrayList<SKU>) -> Unit, onFailure: (Boolean, String, String, Int, String) -> (Unit)) {
        if (skuList.isEmpty()) {
            onFailure(false, "محصولی پیدا نشد", "هیچ محصولی برای خرید پیدا نشد. از متصل بودن به اینترنت مطمئن شو و دوباره امتحان کن.", 200, "NO SKU")
        } else {
            this.skuList.clear()
            this.skuList.addAll(skuList)
            this.onInitSuccess = onSuccess
            this.onInitFailure = onFailure
            if (isAppAvailable(appClass.market.packageName)) {
                startConnection(licenseKey)
            } else {
                onInitFailure(false, "${appClass.market.nameFa} نصب نیست", "برنامه‌ی ${appClass.market.nameFa} رو نصب کن و دوباره امتحان کن.", 202, "Market Not Installed : ${appClass.market.type.name}")
            }
        }
    }

    private fun startConnection(licenseKey: String) {
        iabHelper = IabHelper(licenseKey, appClass)
        initialized = true
        errorTrace += ",1"
        iabHelper.startSetup { result ->
            if (result.isSuccess) {
                errorTrace += ",34"
                iabHelper.queryInventoryAsync(true, skuList.map { it.sku }) { queryResult, inventory ->
                    if (queryResult.isSuccess && inventory != null) {
                        this.inventory = inventory
                        skuList.forEach {
                            it.nativeSKUDetails = inventory.getSkuDetails(it.sku)
                            it.nativePurchase = inventory.getPurchase(it.sku)
                        }
                        onInitSuccess(skuList)
                    } else {
                        onInitFailure(true, "لاگین نکردی", "در فروشگاه لاگین کن و دوباره امتحان کن.", 201, "Need Login")
                    }
                }
            } else {
                errorTrace += ",35"
                onInitFailure(false, "فروشگاه در دسترس نیست", "ارتباط با فروشگاه برقرار نشد. از متصل بودن به اینترنت مطمئن شو و دوباره امتحان کن.", 202, "CODE:${result.response},MESSAGE:${result.message}:TRACE:$errorTrace")
            }
        }
    }

    fun purchase(payload: String, sku: String, onSuccess: (SKU) -> Unit, onFailure: (Boolean, String, String, Int, String) -> Unit) {
        this.onPurchaseSuccess = onSuccess
        this.onPurchaseFailure = onFailure
        skuList.firstOrNull { it.sku == sku }?.let {
            if (it.type == SKU.SKUType.SUB) {
                iabHelper.launchSubscriptionPurchaseFlow(activity, it.sku, 926, { result, purchase -> onPurchaseUpdated(result, purchase, payload) }, payload)
            } else {
                iabHelper.launchPurchaseFlow(activity, it.sku, 926, { result, purchase -> onPurchaseUpdated(result, purchase, payload) }, payload)
            }
        } ?: kotlin.run {
            onPurchaseFailure(false, "خرید ناموفق", "مشکلی موقع خرید پیش اومده. از متصل بودن به اینترنت مطمئن شو و دوباره امتحان کن.", 203, "SKU Not Found : $sku")
        }
    }

    private fun onPurchaseUpdated(result: IabResult?, purchase: Purchase?, payload: String) {
        if (result != null && result.isSuccess && purchase != null) {
            if (payload == purchase.developerPayload) {
                skuList.firstOrNull { it.sku == purchase.sku }?.let {
                    it.nativePurchase = purchase
                    it.payload = payload
                    if (purchase.token[0] == 'b' && purchase.token[1] == '\'' && purchase.token[purchase.token.length - 1] == '\'') {
                        val token = purchase.token.substring(2, purchase.token.length - 1)
                        it.token = token
                    } else {
                        it.token = purchase.token
                    }
                    onPurchaseSuccess(it)
                } ?: kotlin.run {
                    onPurchaseFailure(false, "خرید ناموفق", "مشکلی در خرید بوجود اومده! اگر خرید انجام شده ولی سکه نگرفتی روی دکمه‌ی بازیابی خرید بزن.", 204, "SKU Not Found : ${purchase.sku}")
                }
            } else {
                onPurchaseFailure(false, "خرید ناموفق", "مشکلی در خرید بوجود اومده! اگر خرید انجام شده ولی سکه نگرفتی روی دکمه‌ی بازیابی خرید بزن.", 208, "Payload Not Match : ${payload.length}:${purchase.developerPayload.length}")
            }
        } else {
            onPurchaseFailure(
                arrayOf(IabHelper.IABHELPER_BAD_RESPONSE, IabHelper.IABHELPER_USER_CANCELLED).contains(result?.response),
                "خرید ناموفق",
                "مشکلی در خرید بوجود اومده! اگر خرید انجام شده ولی سکه نگرفتی روی دکمه‌ی بازیابی خرید بزن.",
                205,
                "CODE:${result?.response},MESSAGE:${result?.message}"
            )
        }
    }

    fun getPurchaseList(onSuccess: (ArrayList<SKU>) -> Unit) {
        iabHelper.queryInventoryAsync(true, skuList.map { it.sku }) { queryResult, inventory ->
            if (queryResult.isSuccess) {
                this.inventory = inventory
                skuList.forEach {
                    it.nativeSKUDetails = inventory.getSkuDetails(it.sku)
                    it.nativePurchase = inventory.getPurchase(it.sku)
                }
                onSuccess(ArrayList(skuList.filter { it.nativePurchase != null }))
            } else {
                onSuccess(ArrayList(skuList.filter { it.nativePurchase != null }))
            }
        }
    }

    fun handlePurchase(purchase: Purchase, onSuccess: (SKU) -> Unit = {}, onFailure: (Boolean, String, String, Int, String) -> Unit = { _, _, _, _, _ -> }) {
        skuList.firstOrNull { it.sku == purchase.sku }?.let { it ->
            it.nativePurchase = purchase
            when (it.type) {
                SKU.SKUType.PRO -> {
                    onSuccess(it)
                }
                SKU.SKUType.CON -> {
                    iabHelper.consumeAsync(purchase) { purchase, result ->
                        if (purchase != null && result != null && result.isSuccess) {
                            it.nativePurchase = purchase
                            onSuccess(it)
                        } else {
                            onFailure(false, "خرید ناموفق", "مشکلی در خرید بوجود اومده! اگر خرید انجام شده ولی سکه نگرفتی روی دکمه‌ی بازیابی خرید بزن.", 206, "CODE:${result.response},MESSAGE:${result.message}")
                        }
                    }
                }
                SKU.SKUType.SUB -> {
                    onSuccess(it)
                }
            }
        } ?: kotlin.run {
            onFailure(false, "خرید ناموفق", "مشکلی در خرید بوجود اومده! اگر خرید انجام شده ولی سکه نگرفتی روی دکمه‌ی بازیابی خرید بزن.", 207, "SKU Not Found :${purchase.sku}")
        }
    }

    fun close() {
        if (this::iabHelper.isInitialized) {
            iabHelper.dispose()
        }
    }
}