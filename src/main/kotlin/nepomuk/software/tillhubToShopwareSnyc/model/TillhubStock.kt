package nepomuk.software.tillhubToShopwareSnyc.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class TillhubStockResponse(
        val status: Int,
        val msg: String,
        val request: Request,
        val count: Int,
        val results: Array<TillhubStock?>?
) {
    class Deserializer : ResponseDeserializable<TillhubStockResponse> {
        override fun deserialize(content: String): TillhubStockResponse = Gson().fromJson(content, TillhubStockResponse::class.java)
    }
}


data class TillhubStock(
        val id: String,
        val created_at: CreatedAt,
        val updated_at: UpdatedAt,
        val article: String?,
        val product: String?,
        val qty: Int?,
        val branch: String?,
        val status: String?,
        val metadate: String?,
        val location_type: String?,
        val location: String?
)