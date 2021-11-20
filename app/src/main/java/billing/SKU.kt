package billing

import androidx.databinding.BaseObservable
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

data class SKU(
    var sku: String = "",
    var type: SKUType = SKUType.PRO,
    var payload: String = "",
    var token: String = "",
    var googleSKUDetails: SkuDetails? = null,
    var googlePurchase: Purchase? = null,
    var nativeSKUDetails: billing.SkuDetails? = null,
    var nativePurchase: billing.Purchase? = null,
) : BaseObservable() {


    constructor(item: SKU) : this(
        item.sku,
        item.type,
        item.payload,
        item.token,
        item.googleSKUDetails,
        item.googlePurchase,
        item.nativeSKUDetails,
        item.nativePurchase,
    )

    enum class SKUType {
        PRO,
        CON,
        SUB,
    }

    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("sku", this.sku)
            jsonObject.put("type", this.type)
            jsonObject.put("payload", this.payload)
            jsonObject.put("token", this.token)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return jsonObject
    }

    companion object {

        fun init(response: String): ArrayList<SKU> {
            val list = ArrayList<SKU>()
            try {
                val jsonArray = JSONObject(response).getJSONArray("items")
                for (i in 0 until jsonArray.length()) {
                    list.add(toModel(jsonArray.getJSONObject(i)))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return list
        }


        fun toJson(userList: ArrayList<SKU>): JSONArray {
            val jsonArray = JSONArray()
            try {
                for (i in userList.indices) {
                    jsonArray.put(userList[i].toJson())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return jsonArray
        }

        fun toModel(jsonArray: JSONArray): ArrayList<SKU> {
            val list = ArrayList<SKU>()
            try {
                for (i in 0 until jsonArray.length()) {
                    list.add(toModel(jsonArray.getJSONObject(i)))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return list
        }

        fun toModel(jsonObject: JSONObject): SKU {
            val item = SKU()
            try {
                item.sku = jsonObject.getString("sku")
                item.type = SKUType.valueOf(jsonObject.getString("type"))
                item.payload = jsonObject.getString("payload")
                item.token = jsonObject.getString("token")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return item
        }


        fun cloneList(requestList: ArrayList<SKU>): ArrayList<SKU> {
            val clonedList = ArrayList<SKU>(requestList.size)
            for (item in requestList) {
                clonedList.add(SKU(item))
            }
            return clonedList
        }
    }
}
