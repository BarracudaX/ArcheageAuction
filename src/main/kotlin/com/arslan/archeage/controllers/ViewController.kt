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
        val packs = packService.packs(useContinent)
        return preparePacksView(packs,model,useContinent,Optional.empty(),Optional.empty())
    }

    @GetMapping("/packs", params = ["departureLocation","destinationLocation"])
    fun packs(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam departureLocation: String, @RequestParam destinationLocation: String): String {
        val useContinent = continent.getOrElse { Continent.values()[0] }
        val packs = packService.packs(useContinent,departureLocation,destinationLocation)
        return preparePacksView(packs,model,useContinent, Optional.of(destinationLocation),Optional.of(departureLocation))
    }

    @GetMapping("/packs",params=["departureLocation"])
    fun packsWithDepartureLocation(model: Model,@RequestParam continent: Optional<Continent>,@RequestParam departureLocation: String) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        val packs = packService.packsAt(useContinent,departureLocation)

        return preparePacksView(packs,model,useContinent,Optional.empty(), Optional.of(departureLocation))
    }

    @GetMapping("/packs",params = ["destinationLocation"])
    fun packsWithDestinationLocation(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam destinationLocation: String) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        val packs = packService.packsTo(useContinent,destinationLocation)

        return preparePacksView(packs,model,useContinent,Optional.of(destinationLocation), Optional.empty())
    }

    private fun preparePacksView(packs: List<PackDTO>, model: Model, continent: Continent, destinationLocation: Optional<String>, departureLocation: Optional<String>) : String{
        model.addAttribute("locations", locationService.continentLocations(continent).map(Location::name).minus(destinationLocation.getOrElse { "" }))
        model.addAttribute("factories", locationService.continentFactories(continent).map(Location::name).minus(departureLocation.getOrElse { "" }))
        model.addAttribute("packs", packs)
        model.addAttribute("selectedContinent",continent)
        model.addAttribute("departureLocation",departureLocation)
        model.addAttribute("destinationLocation",destinationLocation)
        model.addAttribute("materials",packs.flatMap { pack -> pack.recipeDTO.materials }.filter{ material -> material.price != null })

        return "packs"
    }
}