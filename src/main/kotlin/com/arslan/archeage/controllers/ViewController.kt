package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.ItemPrice
import com.arslan.archeage.entity.PackPrice
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.LocationService
import com.arslan.archeage.service.PackService
import com.arslan.archeage.toDTO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/")
@Controller
class ViewController(private val locationService: LocationService, private val packService: PackService,private val itemPriceService: ItemPriceService) {

    @GetMapping
    fun homePage() : String = "index"

    @GetMapping("/packs")
    fun packsPage(model: Model) : String {
        val continent = Continent.values()[0]
        val packs = packService.packs(continent)
        val prices = itemPriceService
            .latestPrices(packs.flatMap { pack -> pack.recipes.flatMap { recipe -> recipe.materials.map(CraftingMaterial::item) } })
            .associateBy{ itemPrice -> itemPrice.item.name }

        val packDTOs = packs.flatMap { pack ->
            pack.prices.filterIsInstance<PackPrice>().map { packPrice -> PackDTO(pack.name,pack.creationLocation.name,packPrice.sellLocation.name,packPrice.price,pack.recipes.map { recipe -> recipe.toDTO(prices) }) }
        }
        model.addAttribute("locations",locationService.continentLocations(continent))
        model.addAttribute("factories",locationService.continentFactories(continent))
        model.addAttribute("packs",packDTOs)
        return "packs"
    }
}