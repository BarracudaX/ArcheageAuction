package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.*
import com.arslan.archeage.materialsWithPrice
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.LocationService
import com.arslan.archeage.service.PackService
import org.apache.commons.lang3.arch.Processor.Arch
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.Optional
import kotlin.jvm.optionals.getOrElse

@RequestMapping("/",produces = [MediaType.TEXT_HTML_VALUE])
@Controller
class ViewController(
    private val locationService: LocationService,
    private val packService: PackService
) {

    @GetMapping("/login")
    fun loginPage() : String = "login"


    @GetMapping("/profile")
    fun profile(pageable: Pageable, model: Model) : String = "profile"

    @GetMapping
    fun homePage(): String = "index"

    @GetMapping("/register")
    fun registerPage(model: Model) : String {
        model.addAttribute("registrationForm",RegistrationForm())
        return "register"
    }

    @GetMapping("/packs")
    fun allPacksView(model: Model,continent: Optional<Continent>,archeageServer: ArcheageServer?) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        if(archeageServer == null) return preparePacksView(emptyList(),model,useContinent)
        val packs = packService.packs(useContinent,archeageServer)
        return preparePacksView(packs,model,useContinent,Optional.empty(),Optional.empty())
    }

    @GetMapping("/packs", params = ["departureLocation","destinationLocation"])
    fun packs(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam departureLocation: String, @RequestParam destinationLocation: String,archeageServer: ArcheageServer?): String {
        val useContinent = continent.getOrElse { Continent.values()[0] }
        if(archeageServer == null) return preparePacksView(emptyList(),model,useContinent,Optional.of(destinationLocation),Optional.of(departureLocation))
        val packs = packService.packs(useContinent,departureLocation,destinationLocation,archeageServer)
        return preparePacksView(packs,model,useContinent, Optional.of(destinationLocation),Optional.of(departureLocation))
    }

    @GetMapping("/packs",params=["departureLocation"])
    fun packsWithDepartureLocation(model: Model,@RequestParam continent: Optional<Continent>,@RequestParam departureLocation: String,archeageServer: ArcheageServer?) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        if(archeageServer == null) return preparePacksView(emptyList(),model,useContinent,departureLocation = Optional.of(departureLocation))
        val packs = packService.packsCreatedAt(useContinent,departureLocation,archeageServer)

        return preparePacksView(packs,model,useContinent,Optional.empty(), Optional.of(departureLocation))
    }

    @GetMapping("/packs",params = ["destinationLocation"])
    fun packsWithDestinationLocation(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam destinationLocation: String,archeageServer: ArcheageServer?) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        if(archeageServer == null) return preparePacksView(emptyList(),model,useContinent,destinationLocation = Optional.of(destinationLocation))
        val packs = packService.packsSoldAt(useContinent,destinationLocation,archeageServer)

        return preparePacksView(packs,model,useContinent,Optional.of(destinationLocation))
    }

    private fun preparePacksView(packs: List<PackDTO>, model: Model, continent: Continent, destinationLocation: Optional<String> = Optional.empty(), departureLocation: Optional<String> = Optional.empty()) : String{
        model.addAttribute("locations", locationService.continentLocations(continent).map(Location::name).minus(destinationLocation.getOrElse { "" }))
        model.addAttribute("factories", locationService.continentFactories(continent).map(Location::name).minus(departureLocation.getOrElse { "" }))
        model.addAttribute("packs", packs)
        model.addAttribute("selectedContinent",continent)
        model.addAttribute("departureLocation",departureLocation)
        model.addAttribute("destinationLocation",destinationLocation)
        model.addAttribute("materials",packs.materialsWithPrice())

        return "packs"
    }
}