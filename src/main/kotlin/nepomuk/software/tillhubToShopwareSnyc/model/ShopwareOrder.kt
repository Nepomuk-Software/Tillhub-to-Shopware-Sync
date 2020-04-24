package nepomuk.software.tillhubToShopwareSnyc.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class ShopwareOrder(
        val data: ShopwareOrderData
) {
    class Deserializer : ResponseDeserializable<ShopwareOrder> {
        override fun deserialize(content: String): ShopwareOrder = Gson().fromJson(content, ShopwareOrder::class.java)
    }
}


data class ShopwareOrderData(
        val id: Int?,
        val changed: String?,
        val number: String?,
        val customerId: Int?,
        val paymentId: Int?,
        val dispatchId: Int?,
        val shopId: Int?,
        val invouceAmount: Float?,
        val invoiceAmountNet: Float?,
        val invoiceShipping: Float?,
        val invoiceShippingNet: Float?,
        val invoiceShippingTaxRate: Int?,
        val orderTime: String?,
        val transactionId: String?,
        val comment: String?,
        val customerComment: String?,
        val internalComment: String?,
        val net: Int?,
        val taxFree: Int?,
        val temporaryId: String?,
        val referer: String?,
        val clearedDate: String?,
        val trackingCode: String?,
        val languageIso: String?,
        val currency: String?,
        val currencyFactor: Int?,
        val remoteAddress: String?,
        val deviceType: String?,
        val isProportionalCalculation: Boolean?,
        val details: Array<ShopwareOrderDataDetails?>?
)

data class ShopwareOrderDataDetails(
        val id: Int?,
        val orderId: Int?,
        val articleId: Int?,
        val taxId: Int?,
        val statusId: Int?,
        val articleDetailID: Int?,
        val number: String?,
        val articleNumer: String?,
        val price: Float?,
        val quantity: Int?,
        val articleName: String?,
        val shipped: Int?,
        val shippedGroup: Int?,
        val releaseDate: String?,
        val mode: Int?,
        val esdArticle: Int?,
        val config: String?,
        val ean: String?,
        val unit: String?,
        val packUnit: String?,
        val attribute: ShopwareOrderDataDetailsAttribute?
)

data class ShopwareOrderDataDetailsAttribute(
        val id: Int,
        val orderDetailId: Int,
        val attribute1: String,
        val attribute2: String?,
        val attribute3: String?,
        val attribute4: String?,
        val attribute5: String?,
        val attribute6: String?
)
