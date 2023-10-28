package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.RecipeDTO
import com.arslan.archeage.entity.*
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.LocationService
import com.arslan.archeage.service.PackService
import com.arslan.archeage.toDTO
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.Optional
import kotlin.jvm.optionals.getOrElse

@RequestMapping("/")
@Controller
class ViewController(
    private val locationService: LocationService,
    private val packService: PackService,
    private val itemPriceService: ItemPriceService
) {

    @GetMapping
    fun homePage(): String = "index"

    @GetMapping("/packs")
    fun allPacksView(model: Model,continent: Optional<Continent>) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        return preparePacksView(packService.packs(useContinent),model,useContinent,Optional.empty(),Optional.empty())
    }

    @GetMapping("/packs", params = ["departureLocation","destinationLocation"])
    fun packs(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam departureLocation: String, @RequestParam destinationLocation: String): String {
        val useContinent = continent.getOrElse { Continent.values()[0] }
        return preparePacksView(packService.packs(useContinent,departureLocation,destinationLocation),model,useContinent, Optional.of(destinationLocation),Optional.of(departureLocation))
    }

    @GetMapping("/packs",params=["departureLocation"])
    fun packsWithDepartureLocation(model: Model,@RequestParam continent: Optional<Continent>,@RequestParam departureLocation: String) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }

        return preparePacksView(packService.packsAt(useContinent,departureLocation),model,useContinent,Optional.empty(), Optional.of(departureLocation))
    }

    @GetMapping("/packs",params = ["destinationLocation"])
    fun packsWithDestinationLocation(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam destinationLocation: String) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }

        return preparePacksView(packService.packsTo(useContinent,destinationLocation),model,useContinent,Optional.of(destinationLocation), Optional.empty())
    }

    private fun preparePacksView(packs: List<Pack>, model: Model, continent: Continent, destinationLocation: Optional<String>, departureLocation: Optional<String>) : String{
        val prices = itemPriceService
            .latestPrices(packs.flatMap { pack -> pack.recipes.flatMap { recipe -> recipe.materials.map(CraftingMaterial::item) } })
            .associateBy { itemPrice -> itemPrice.item.name }

        val packDTOs = packs.flatMap { pack ->
            pack.prices
                .filterIsInstance<PackPrice>()
                .flatMap { packPrice ->
                    pack.recipes.map { recipe ->
                        PackDTO(pack.name, pack.creationLocation.name, packPrice.sellLocation.name, packPrice.price, RecipeDTO(recipe.producedQuantity,recipe.materials.map { material -> material.toDTO(prices) },recipe.id!!))
                    }
                }
        }.sortedByDescending(PackDTO::profit)

        model.addAttribute("locations", locationService.continentLocations(continent).map(Location::name).minus(destinationLocation.getOrElse { "" }))
        model.addAttribute("factories", locationService.continentFactories(continent).map(Location::name).minus(departureLocation.getOrElse { "" }))
        model.addAttribute("packs", packDTOs)
        model.addAttribute("selectedContinent",continent)
        model.addAttribute("departureLocation",departureLocation)
        model.addAttribute("destinationLocation",destinationLocation)

        return "packs"
    }
}