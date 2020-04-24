package nepomuk.software.tillhubToShopwareSnyc.model

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

data class TillhubResponse(
        val status: Int?,
        val msg: String?,
        val request: Request?,
        val cursor: Cursor?,
        val count: Int?,
        val results: Array<TillhubArticle?>
) {
    class Deserializer : ResponseDeserializable<TillhubResponse> {
        override fun deserialize(content: String): TillhubResponse = Gson().fromJson(content, TillhubResponse::class.java)
    }
}

data class Request(
        val host: String?,
        val id: String?,
        val cursor: RequestCursor?
)

data class RequestCursor(
        val first: RequestCursorFirst?,
        val page: Int?
)

data class RequestCursorFirst(
        val id: String?,
        val updated_at: String?,
        val page: Int?,
        val cursor_field: String?
)

data class Cursor(
        val first: String?,
        val self: String?
)

data class TillhubArticle(
        val children: String?,
        val id: String?,
        val custom_id: String?,
        val metadata: JvmType.Object?,
        val created_at: CreatedAt?,
        val updated_at: UpdatedAt?,
        val name: String?,
        val summary: String?,
        val description: String?,
        val account: String?,
        val tax: String?,
        val vat_class: String?,
        val category: String?,
        val brand: String?,
        val images: Map<String?, String?>?,
        val condition: String?,
        val manufacturer: Manufacturer?,
        val produced_at: String?,
        val purchased_at: String?,
        val release_at: String?,
        val similar_to: Array<String?>,
        val related_to: Array<String?>,
        val audiences: Array<String?>,
        val keywords: Array<String?>,
        val categories: Array<String?>,
        val custom_ids: Array<String?>,
        val active: Boolean?,
        val deleted: Boolean?,
        val type: String?,
        val inser_id: String?,
        val product_group: String?,
        val supplier: Supplier?,
        val attributes: Map<String, String>,
        val barcode: String?,
        val prices: TillhubArticlePrices?,
        val sku: String?,
        val stock_mininum: String?,
        val stockable: String?,
        val taxes_option: String?,
        val parent: String?,
        val linked_products: String?,
        val season: String?,
        val seasons: String?,
        val tags: Array<String>?,
        val delegated_to: String?,
        val stock_maximum: String?,
        val codes: Array<CodesObject?>,
        val reorder_point: String?,
        val reorder_qty: String?,
        val external_reference_id: String?,
        val client_id: String?,
        val default_tile_color: String?,
        val discountable: String?,
        val linkable: String?,
        val stock_configuration_location: String?,
        val stock_mode: String?,
        val locations: String?,
        val is_service: String?,
        val service_questions: Array<String?>,
        val service_question_groups: Array<String?>,
        val configuration: Configuration?,
        val loyalty_values: String?,
        val manufacturers: String?,
        val delegated_from: String?,
        val delegateable_to: String?,
        val delegateable: Boolean?,
        val delegatable: Boolean?,
        val delegatable_to: Boolean?,
        val warranty_notice: String?,
        val refund_policy: String?,
        val disclaimer: String?,
        val policy: String?,
        val online: Boolean?,
        val shipping_required: Boolean?,
        val options: String?
)

data class CodesObject(
        val data: String?,
        val caption: String?,
        val type: String?
)

data class CreatedAt(
        val iso: String?,
        val unix: Long?
)

data class UpdatedAt(
        val iso: String?,
        val unix: Long?
)

data class Manufacturer(
        val iln: String?
)

data class Supplier(
        val sku: String?
)

data class TillhubArticlePrices(
        val default_prices: Array<DefaultPrices?>?,
        val currency: String?,
        val percentage: String?,
        val purchase_price: Long?
)

data class DefaultPrices(
        val amount: Amount?,
        val currency: String?,
        val percentage: Int?,
        val purchase_price: String?,
        val cost: String?,
        val margin: Int?
)

data class Amount(
        val net: String?,
        val gross: String = "9999"
)

data class Configuration(
        val pricing: Pricing?,
        val allow_zero_prices: Boolean?
)

data class Pricing(
        val allow_is_free: Boolean?
)