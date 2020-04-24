package nepomuk.software.tillhubToShopwareSnyc.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.util.*

data class ShopwareMediaResponse(
        var data: Array<ShopwareMedia>
) {
    class Deserializer : ResponseDeserializable<ShopwareMediaResponse> {
        override fun deserialize(content: String): ShopwareMediaResponse = Gson().fromJson(content, ShopwareMediaResponse::class.java)
    }
}


data class ShopwareMedia(
        var id: Int?,
        var albumId: Int?,
        var name: String?,
        var description: String?,
        var path: String?,
        var type: String?,
        var extension: String?,
        var userId: Int?,
        var created: Date?,
        var fileSize: Int?
)

data class CreateShopwareMedia(
        var album: Int,
        var name: String,
        var file: String,
        var description: String
)