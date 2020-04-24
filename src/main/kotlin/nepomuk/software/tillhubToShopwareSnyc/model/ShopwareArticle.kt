package nepomuk.software.tillhubToShopwareSnyc.model

data class ShopwareArticle(
        var name: String?,
        var description: String?,
        var descriptionLong: String?,
        val active: Boolean,
        val tax: Int,
        var supplier: String?,
        val categories: Array<Categories>,
        val lastStock: Boolean = true,
        val mainDetail: MainDetails,
        val __options_images: ImageOptions,
        val images: Array<ShopwareImages>
        )

data class Categories (
        var id: Int
)

data class MainDetails(
        var number: String?,
        val active: Boolean,
        val inStock: Int?,
        val prices: Array<ShopwareArticlePrices>
)

data class ShopwareArticlePrices(
        val customerGroupKey: String?,
        var price: Float
)

data class ImageOptions(
        val replace: Boolean
)

data class ShopwareImages(
        val mediaId: Int?,
        val link: String?
)