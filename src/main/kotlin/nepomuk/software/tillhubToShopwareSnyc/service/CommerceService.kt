package nepomuk.software.tillhubToShopwareSnyc.service

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.authentication
import com.google.gson.Gson
import nepomuk.software.tillhubToShopwareSnyc.model.*
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CommerceService {

    val shopwareArticleApiUrl = "https://shopwareshop.de/api/articles"
    val shopwareCategoryApiUrl = "https://shopwareshop.de/api/categories"
    val shopwareMediaApiUrl = "https://shopwareshop.de/api/media"
    val shopwareApiUser = "user"
    val shopwareApiKey = "key"
    val token = "tillhubtoken"
    val user = "tillhubuserid"
    val tillhubProductsApiUrl = "https://api.tillhub.com/api/v1/products"
    val tillhubProductGroupsApiUrl = "https://api.tillhub.com/api/v0/product_groups"
    val tillhubProductStockApiUrl = "https://api.tillhub.com/api/v0/stock"

    val logger = LoggerFactory.getLogger("CONSOLE_JSON_APPENDER")

    val gson = Gson()

    fun createProductGroupsInShopware(productGroups: Array<ProductGroup?>) {

        productGroups.forEach {
            val shopwareCategory = ShopwareCategory(
                    it!!.name.toString(),
                    1
            )

            val (_, _, _) = Fuel.post("$shopwareCategoryApiUrl/shopwareCategory.parentId")
                    .authentication().basic(shopwareApiUser, shopwareApiKey)
                    .body(gson.toJson(shopwareCategory))
                    .responseString()
        }
    }

    fun getAllProductGroupsFromTillhub(): Array<ProductGroup?> {
        val (_, _, result) = Fuel.get("$tillhubProductGroupsApiUrl/$user")
                .header(mapOf("Authorization" to "Bearer $token")).responseObject(TillhubProductGroup.Deserializer())

        val (payload, _) = result

        return payload!!.results
    }

    fun setArticleToActivePassiveState(tillhubArticle: TillhubArticle): Boolean {
        if (tillhubArticle.brand == null || tillhubArticle.brand.isEmpty()) {
            return false
        }
        return true
    }

    fun checkForDescription(tillhubArticle: TillhubArticle): String? {
        if (tillhubArticle.description.isNullOrBlank()) {
            return ""
        }
        return tillhubArticle.description
    }

    fun mapTillhubToShopwareArticle(tillhubArticle: TillhubArticle, articleGroupId: String?, stock: Int?, mediaId: Int): ShopwareArticle {
        val productGroup = getProductGroup(tillhubArticle.product_group!!)

        return ShopwareArticle(
                name = tillhubArticle.name ?: throw IllegalArgumentException("Name required"),
                description = checkForDescription(tillhubArticle),
                descriptionLong = checkForDescription(tillhubArticle),
                active = setArticleToActivePassiveState(tillhubArticle),
                tax = 19,
                supplier = tillhubArticle.brand,
                categories = arrayOf(getCategory(productGroup!![0]!!.name, tillhubArticle.name)),
                lastStock = true,
                mainDetail = MainDetails(
                        tillhubArticle.custom_id ?: throw IllegalArgumentException("Id required"),
                        setArticleToActivePassiveState(tillhubArticle),
                        stock ?: throw IllegalArgumentException("Stock required"),
                        arrayOf(ShopwareArticlePrices(
                                "EK",
                                getPrice(tillhubArticle.prices?.default_prices?.get(0)?.amount?.gross
                                        ?: throw IllegalArgumentException("Price required"))
                        ))
                ),
                __options_images = (ImageOptions(true)),
                images = arrayOf(ShopwareImages(mediaId, extractImageFromTillhubArticle(tillhubArticle.images)))
        )
    }

    fun syncImages(tillhubArticle: TillhubArticle): Int {
        uploadImageToShopware(tillhubArticle)
        return getImagesFromShopware(tillhubArticle.name!!)

    }

    fun getImagesFromShopware(productName: String): Int {
        val (_, _, result) = Fuel.get(shopwareMediaApiUrl)
                .authentication().basic("wolke7api", "WVOkuKlaZBtrgrtNzuKSrIOPyV2S4sTmT2tRExQQ")
                .header("Content-Type" to "application/json")
                .responseObject(ShopwareMediaResponse.Deserializer())

        val (payload, _) = result

        payload!!.data.forEach {
            if (it.description == productName) {
                return it.id!!
            }
        }
        return 0
    }

    fun uploadImageToShopware(tillhubArticle: TillhubArticle): Array<ShopwareImages> {

        if (tillhubArticle.images == null) {
            return emptyArray()
        }

        val image = CreateShopwareMedia(
                //folder files in shopware. why "-"? idk
                -6,
                tillhubArticle.name!!,
                extractImageFromTillhubArticle(tillhubArticle.images),
                tillhubArticle.name
        )

        val imageJson = gson.toJson(image)

        val (_, _, _) = Fuel.post(shopwareMediaApiUrl)
                .authentication().basic(shopwareApiUser, shopwareApiKey)
                .header("Content-Type" to "application/json")
                .body(imageJson)
                .responseString()

        return arrayOf(ShopwareImages(2, null))
    }

    fun extractImageFromTillhubArticle(images: Map<String?, String?>?): String {
        val fallbackImage = "https://shopwareShop.de/media/image/0e/6c/a7/Logo_freigestellt.png"

        return images!!["original"] ?: fallbackImage
    }

    fun getCategory(tillhubCategory: String?, productName: String): Categories {
        return when (tillhubCategory) {
            "Shisha" -> Categories(4)
            "Tabak" -> Categories(5)
            "Zubehör" -> Categories(6)
            "Kohle" -> Categories(7)
            "Liquids" -> Categories(8)
            "E-Zigaretten" -> Categories(9)
            "Tabak Ersatz" -> Categories(10)
            else -> {
                val msg = "$productName: category not found"
                logger.error(msg)
                sendChatMessage(msg)
                return Categories(99999)
            }
        }
    }

    fun getPrice(tillhubArticlePrice: String?): Float {
        if (tillhubArticlePrice.isNullOrBlank()) {
            return 99999F
        }
        return tillhubArticlePrice.toFloat()
    }

    fun createArticleInShopware(shopwareArticleJson: String, id: String): Response {

        val (_, response, result) = Fuel.post("$shopwareArticleApiUrl/${id}")
                .authentication().basic(shopwareApiUser, shopwareApiKey)
                .body(shopwareArticleJson)
                .responseString()
        return response
    }

    fun updateArticle(shopwareArticleJson: String, id: String) {

        val (_, _, _) = Fuel.put("$shopwareArticleApiUrl/${id}?useNumberAsId=true")
                .authentication().basic(shopwareApiUser, shopwareApiKey)
                .body(shopwareArticleJson)
                .responseString()
    }

    fun getAllProducts(): TillhubResponse? {

        val (_, _, result) = Fuel.get("$tillhubProductsApiUrl/$user")
                .header(mapOf("Authorization" to "Bearer $token"))
                .responseObject(TillhubResponse.Deserializer())

        val (payload, _) = result

        return payload
    }

    fun getProductGroup(producGroupId: String): Array<ProductGroup?>? {
        val (_, _, result) = Fuel.get("$tillhubProductGroupsApiUrl/$user/$producGroupId")
                .header(mapOf("Authorization" to "Bearer $token")).responseObject(TillhubProductGroup.Deserializer())

        val (payload, _) = result

        if (payload == null) {
            return arrayOf(ProductGroup(null, null, null, null, null, null,
                    emptyArray(), "unknown", null, null, null, emptyArray()))
        }
        return payload.results
    }

    fun getStock(): Array<TillhubStock?>? {
        val (_, _, result) = Fuel.get("$tillhubProductStockApiUrl/$user")
                .header(mapOf("Authorization" to "Bearer $token")).responseObject(TillhubStockResponse.Deserializer())

        val (payload, _) = result

        return payload!!.results!!
    }

    fun findStockForProduct(tillhubArticle: TillhubArticle, stock: Array<TillhubStock?>?): Int? {

        val articleStock = stock!!.find { it!!.product == tillhubArticle.id }

        if (articleStock?.qty == null) {
            return 0
        }

        return articleStock.qty
    }

    fun sendChatMessage(msg: String) {
        val telegramApiUrl = "https://api.telegram.org"
        val telegramApiToken = "someTelegranApiToken"
        val chatId = "someChatId"

        val (_, _, _) = Fuel.post("$telegramApiUrl/bot$telegramApiToken/sendMessage", listOf("chat_id" to chatId, "text" to msg))
                .responseString()
    }

    fun syncImagesToShopware() {
        val products = getAllProducts()
        val imageCount = products!!.results.count()

        sendChatMessage("Der Sync für $imageCount Bilder ist gestartet")
        products.results.forEachIndexed { index, it ->
            if (index % 100 == 0) {
                val percentage = index * 100 / imageCount
                sendChatMessage("$percentage%")
            }
            syncImages(it!!)
        }
        sendChatMessage("Fertig! Alle Bilder sind in Shopware angelegt.")
    }

    @Scheduled(cron = "0 1 1 * * ?")
    fun syncArticleToShopware() {
        val products = getAllProducts()
        val stock = getStock()
        val productCount = products!!.results.count()

        sendChatMessage("Der Sync für $productCount Produkte ist gestartet")
        products.results.forEachIndexed { index, it ->
            if (index % 100 == 0) {
                val percentage = index * 100 / productCount
                sendChatMessage("$percentage%")
            }

            val image = getImagesFromShopware(it!!.name!!)

            val shopwareArticle = mapTillhubToShopwareArticle(it, "3", findStockForProduct(it, stock), image)

            val response = createArticleInShopware(gson.toJson(shopwareArticle), it.custom_id!!)

            when (response.statusCode) {
                200 -> logger.info("${it.custom_id} ${it.name}: created")
                201 -> logger.info("${it.custom_id} ${it.name}: created")
                400 -> updateArticle(gson.toJson(shopwareArticle), it.custom_id)
                else -> logger.error("${it.custom_id} ${it.name} could not be created. Error message: {}" + response.body())
            }

        }

        sendChatMessage("Fertig! Alle Produkte sind in Shopware angelegt.")
    }
}