package nepomuk.software.tillhubToShopwareSnyc.controller

import nepomuk.software.tillhubToShopwareSnyc.service.CommerceService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CommerceController(val commerceService: CommerceService) {

    @PostMapping("/deltasync")
    fun deltaSync() {
        commerceService.syncArticleToShopware()
    }

    @PostMapping("/imagesync")
    fun imageSync() {
        commerceService.syncImagesToShopware()
    }

    @PostMapping("/productgroups")
    fun syncProductGroups() {
        val productGroups = commerceService.getAllProductGroupsFromTillhub()
        val shisha = productGroups.filter { it -> it!!.name == "Shisha" }.first()
        val tabak = productGroups.filter { it -> it!!.name == "Tabak" }.first()
        val zubehör = productGroups.filter { it -> it!!.name == "Zubehör" }.first()
        val kohle = productGroups.filter { it -> it!!.name == "Kohle" }.first()
        val liquids = productGroups.filter { it -> it!!.name == "Liquids" }.first()
        val ezigaretten = productGroups.filter { it -> it!!.name == "E-Zigaretten" }.first()
        val tabakErsatz = productGroups.filter { it -> it!!.name == "Tabak Ersatz" }.first()

        commerceService.createProductGroupsInShopware(arrayOf(shisha, tabak, zubehör, kohle, liquids, ezigaretten, tabakErsatz))
    }

    @PostMapping("/chat")
    fun sendChatMessage() {
        commerceService.sendChatMessage("was")
    }
}