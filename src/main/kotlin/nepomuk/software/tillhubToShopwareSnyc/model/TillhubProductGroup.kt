package nepomuk.software.tillhubToShopwareSnyc.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class TillhubProductGroup(
        val status: String?,
        val msg: String?,
        val request: Request?,
        val count: Int?,
        val results: Array<ProductGroup?>
) {
    class Deserializer : ResponseDeserializable<TillhubProductGroup> {
        override fun deserialize(content: String): TillhubProductGroup = Gson().fromJson(content, TillhubProductGroup::class.java)
    }
}

data class ProductGroup(
        val id: String?,
        var product_group_id: String?,
        val tax: String?,
        val account: String?,
        val created_at: CreatedAt?,
        val updated_at: UpdatedAt?,
        @Transient
        val images: Array<String?>,
        val name: String?,
        val color: String?,
        val active: Boolean?,
        val deleted: Boolean?,
        val metadata: Array<String?>
)