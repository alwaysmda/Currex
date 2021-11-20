package util

import android.app.Activity
import billing.SKU
import com.android.billingclient.api.*
import util.extension.log

class GoogleBillingV4Helper(private val activity: Activity) {

    val skuList = arrayListOf<SKU>()
    private var onQueryInventoryCompleteCount = 0
    private var onQueryPurchaseHistoryCompleteCount = 0
    private var onInitSuccess: (ArrayList<SKU>) -> Unit = {}
    private var onInitFailure: (Boolean, String, String, Int, String) -> Unit = { needLogin, title, desc, code, extra -> }
    private var onPurchaseSuccess: (SKU) -> Unit = { }
    private var onPurchaseFailure: (Boolean, String, String, Int, String) -> Unit = { canceled, title, desc, code, extra -> }


    private var billingClient = BillingClient.newBuilder(activity)
        .setListener(::onPurchaseUpdated)
        .enablePendingPurchases()
        .build()


    fun init(skuList: ArrayList<SKU>, onSuccess: (ArrayList<SKU>) -> Unit, onFailure: (Boolean, String, String, Int, String) -> (Unit)) {
        log("GBV4:FUN:init:${skuList.joinToString { it.sku }}")
        if (skuList.isEmpty()) {
            onFailure(false, "No Product Found", "No purchasable product was found at this moment. Please try again later.", 100, "")
        } else {
            this.skuList.clear()
            this.skuList.addAll(skuList)
            this.onInitSuccess = onSuccess
            this.onInitFailure = onFailure
            startConnection()
        }
    }

    fun purchase(sku: String, onSuccess: (SKU) -> Unit, onFailure: (Boolean, String, String, Int, String) -> Unit) {
        log("GBV4:FUN:purchase:$sku")
        this.onPurchaseSuccess = onSuccess
        this.onPurchaseFailure = onFailure
        purchase(null, sku, null)
    }

    fun purchase(sku: String, previousPlanToken: String, updateMode: Int = BillingFlowParams.ProrationMode.IMMEDIATE_WITH_TIME_PRORATION, onSuccess: (SKU) -> Unit, onFailure: (Boolean, String, String, Int, String) -> Unit) {
        log("GBV4:FUN:purchase:$sku")
        this.onPurchaseSuccess = onSuccess
        this.onPurchaseFailure = onFailure
        purchase(null, sku, previousPlanToken, updateMode)
    }

    fun getPurchaseList(onSuccess: (ArrayList<SKU>) -> Unit) {
        log("GBV4:FUN:getPurchaseList")
        onQueryPurchaseHistoryCompleteCount = 0
        if (skuList.any { it.type != SKU.SKUType.SUB }) {
            getPurchaseList(BillingClient.SkuType.INAPP, onSuccess)
        }
        if (skuList.any { it.type == SKU.SKUType.SUB }) {
            getPurchaseList(BillingClient.SkuType.SUBS, onSuccess)
        }
    }

    private fun startConnection() {
        log("GBV4:FUN:startConnection")
        onQueryInventoryCompleteCount = 0
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                log("GBV4:FUN:onBillingServiceDisconnected")
                startConnection()
            }

            override fun onBillingSetupFinished(result: BillingResult) {
                log("GBV4:FUN:onBillingSetupFinished:${result.responseCode}")
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    val skuPro = skuList.filterNot { it.type == SKU.SKUType.SUB }.map { it.sku }
                    if (skuPro.isNotEmpty()) {
                        querySkuDetails(skuPro, BillingClient.SkuType.INAPP)
                    }
                    val skuSub = skuList.filter { it.type == SKU.SKUType.SUB }.map { it.sku }
                    if (skuSub.isNotEmpty()) {
                        querySkuDetails(skuSub, BillingClient.SkuType.SUBS)
                    }
                } else {
                    onInitFailure(false, "Billing Unavailable", "Could not connect to Google Play. Please check your network connection or restart the application.", 101, "CODE:${result.responseCode},MESSAGE:${result.debugMessage}")
                    //error setup failed
                }
            }
        })
    }

    private fun querySkuDetails(skus: List<String>, type: String) {
        log("GBV4:FUN:querySkuDetails:${skus.joinToString { it }}")
        billingClient.querySkuDetailsAsync(SkuDetailsParams.newBuilder().setSkusList(skus).setType(type).build()) { result2, skuDetails ->
            if (result2.responseCode == BillingClient.BillingResponseCode.OK && skuDetails != null && skuDetails.isNotEmpty()) {
                skuList.forEach { sku ->
                    sku.googleSKUDetails = skuDetails.firstOrNull { sku.sku == it.sku }
                }
                onQueryInventoryComplete()
            } else {
                var skuString = ""
                skus.forEach {
                    skuString += "$it,"
                }
                onInitFailure(false, "Billing Unavailable", "Billing could not be initialized. Please check your network connection or restart the application.", 103, "SKUS:$skuString")
                //error query failed
            }
        }
    }

    private fun onQueryInventoryComplete() {
        log("GBV4:FUN:onQueryInventoryComplete:${skuList.joinToString { "${it.sku}:${it.googleSKUDetails?.price}" }}")
        onQueryInventoryCompleteCount++
        if (skuList.any { it.type == SKU.SKUType.SUB } && skuList.any { it.type != SKU.SKUType.SUB }) {
            if (onQueryInventoryCompleteCount == 2) {
                //success
                onInitSuccess(skuList)
            } else {
                //waiting for second query to complete
            }
        } else {
            //success
            onInitSuccess(skuList)
        }
    }

    private fun purchase(temp: Boolean?, sku: String, previousPlanToken: String?, updateMode: Int = BillingFlowParams.ProrationMode.IMMEDIATE_WITH_TIME_PRORATION) {
        log("GBV4:FUN:purchase:$sku")
        skuList.firstOrNull { it.sku == sku }?.googleSKUDetails?.let { details ->
            val flowParams = BillingFlowParams.newBuilder().setSkuDetails(details)
            previousPlanToken?.let {
                flowParams.setSubscriptionUpdateParams(
                    BillingFlowParams.SubscriptionUpdateParams.newBuilder()
                        .setOldSkuPurchaseToken(previousPlanToken)
                        .setReplaceSkusProrationMode(updateMode).build()
                )
            }

            val result = billingClient.launchBillingFlow(activity, flowParams.build())
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                //success purchase launched (no action needed)
            } else {
                //error purchase launch failed
                onPurchaseFailure(false, "Purchase Failed", "There was an error while purchasing. Please try again.", 104, "CODE:${result.responseCode},MESSAGE:${result.debugMessage}")
            }
        } ?: kotlin.run {
            //error sku not found
            onPurchaseFailure(false, "Purchase Failed", "There was an error while purchasing. Please try again.", 105, "SKU:$sku not found")
        }
    }

    private fun onPurchaseUpdated(result: BillingResult, purchaseList: List<Purchase>?) {
        log("GBV4:FUN:onPurchaseUpdated:${result.responseCode}:${purchaseList?.joinToString { it.skus.joinToString { ss -> ss } }}")
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchaseList != null) {
            for (purchase in purchaseList) {
                //                handlePurchase(purchase)
                purchase.skus.forEach { sku ->
                    skuList.firstOrNull { it.sku == sku }?.let {
                        it.googlePurchase = purchase
                        it.token = purchase.purchaseToken
                        onPurchaseSuccess(it)
                    } ?: kotlin.run {
                        onPurchaseFailure(false, "Purchase Failed", "There was an error while purchasing. Please try again.", 106, "SKU not found : $sku")
                    }
                }
            }
        } else if (result.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            //error purchase canceled
            onPurchaseFailure(true, "Purchase Canceled", "You have canceled the purchase.", 107, "")
        } else {
            //error purchase failed
            onPurchaseFailure(false, "Purchase Failed", "There was an error while purchasing. Please try again.", 108, "CODE:${result.responseCode},MESSAGE:${result.debugMessage}")
        }
    }


    fun handlePurchase(purchase: Purchase, onSuccess: (SKU) -> Unit = {}, onFailure: (Boolean, String, String, Int, String) -> Unit = { _, _, _, _, _ -> }) {
        log("GBV4:FUN:handlePurchase:${purchase.skus.joinToString { it }}")
        purchase.skus.forEach { sku ->
            skuList.firstOrNull { it.sku == sku }?.let { it ->
                it.googlePurchase = purchase
                when (purchase.purchaseState) {
                    Purchase.PurchaseState.PURCHASED         -> {
                        when (it.type) {
                            SKU.SKUType.PRO -> {
                                if (purchase.isAcknowledged) {
                                    onSuccess(it)
                                } else {
                                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                                        .setPurchaseToken(purchase.purchaseToken)
                                    billingClient.acknowledgePurchase(acknowledgePurchaseParams.build()) { result ->
                                        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                            onSuccess(it)
                                        } else {
                                            onFailure(false, "Purchase Failed", "Your purchase was successful but an unknown error has occurred. Please restart restore purchase or restart the application.", 109, "CODE:${result.responseCode},MESSAGE:${result.debugMessage}")
                                        }
                                    }
                                }
                            }
                            SKU.SKUType.CON -> {
                                val consumeParams = ConsumeParams.newBuilder()
                                    .setPurchaseToken(purchase.purchaseToken)
                                    .build()
                                billingClient.consumeAsync(consumeParams) { result, token ->
                                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                                        it.googlePurchase = null
                                        onSuccess(it)
                                    } else {
                                        onFailure(false, "Purchase Failed", "Your purchase was successful but an unknown error has occurred. Please restart restore purchase or restart the application.", 110, "CODE:${result.responseCode},MESSAGE:${result.debugMessage}")
                                    }
                                }
                            }
                            SKU.SKUType.SUB -> {
                                onSuccess(it)
                            }
                        }
                    }
                    Purchase.PurchaseState.PENDING           -> {
                        //handle pending purchase
                        onSuccess(it)
                    }
                    Purchase.PurchaseState.UNSPECIFIED_STATE -> {
                        var skuString = ""
                        purchase.skus.forEach { sku ->
                            skuString += "$sku,"
                        }
                        onFailure(false, "Purchase Failed", "There was an error while purchasing. Please try again.", 111, "STATE:${purchase.purchaseState},SKU:${skuString}")
                    }
                }
            } ?: kotlin.run {
                var skuString = ""
                purchase.skus.forEach {
                    skuString += "$it,"
                }
                onFailure(false, "Purchase Failed", "There was an error while purchasing. Please try again.", 112, "SKU Not Found $sku in Purchase Skus:$skuString")
            }
        }
    }

    private fun getPurchaseList(type: String, onSuccess: (ArrayList<SKU>) -> Unit) {
        log("GBV4:FUN:getPurchaseList")
        billingClient.queryPurchasesAsync(type) { result, purchaseList ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                purchaseList.forEach { purchase ->
                    purchase.skus.forEach { sku ->
                        skuList.firstOrNull { it.sku == sku }?.apply {
                            this.payload = purchase.developerPayload
                            this.token = purchase.purchaseToken
                            this.googlePurchase = purchase
                        }
                    }
                }
                onQueryPurchaseHistoryComplete(onSuccess)
            } else {
                onQueryPurchaseHistoryComplete(onSuccess)
            }
        }
    }

    private fun onQueryPurchaseHistoryComplete(onSuccess: (ArrayList<SKU>) -> Unit) {
        log("GBV4:FUN:onQueryPurchaseHistoryComplete:${skuList.joinToString { "${it.sku}:${it.token}" }}")
        onQueryPurchaseHistoryCompleteCount++
        if (skuList.any { it.type == SKU.SKUType.SUB } && skuList.any { it.type != SKU.SKUType.SUB }) {
            if (onQueryPurchaseHistoryCompleteCount == 2) {
                //success
                onSuccess(ArrayList(skuList.filter { it.googlePurchase != null }))
            } else {
                //waiting for second query to complete
            }
        } else {
            //success
            onSuccess(ArrayList(skuList.filter { it.googlePurchase != null }))
        }
    }
}