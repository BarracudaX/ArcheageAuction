package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.CraftingMaterialDTO
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
        if(archeageServer == null) return prepareEmptyPacksView(model,useContinent)
        val packs = packService.packs(useContinent,archeageServer)
        return preparePacksView(packs,model,useContinent, archeageServer = archeageServer)
    }

    @GetMapping("/packs", params = ["departureLocation","destinationLocation"])
    fun packs(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam departureLocation: Long, @RequestParam destinationLocation: Long,archeageServer: ArcheageServer?): String {
        val useContinent = continent.getOrElse { Continent.values()[0] }
        if(archeageServer == null) return prepareEmptyPacksView(model,useContinent,Optional.of(destinationLocation),Optional.of(departureLocation))
        val packs = packService.packs(useContinent,departureLocation,destinationLocation,archeageServer)
        return preparePacksView(packs,model,useContinent, Optional.of(destinationLocation),Optional.of(departureLocation), archeageServer = archeageServer)
    }

    @GetMapping("/packs",params=["departureLocation"])
    fun packsWithDepartureLocation(model: Model,@RequestParam continent: Optional<Continent>,@RequestParam departureLocation: Long,archeageServer: ArcheageServer?) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        if(archeageServer == null) return prepareEmptyPacksView(model,useContinent,departureLocation = Optional.of(departureLocation))
        val packs = packService.packsCreatedAt(useContinent,departureLocation,archeageServer)

        return preparePacksView(packs,model,useContinent,Optional.empty(), Optional.of(departureLocation), archeageServer = archeageServer)
    }

    @GetMapping("/packs",params = ["destinationLocation"])
    fun packsWithDestinationLocation(model: Model, @RequestParam continent: Optional<Continent>, @RequestParam destinationLocation: Long,archeageServer: ArcheageServer?) : String{
        val useContinent = continent.getOrElse { Continent.values()[0] }
        if(archeageServer == null) return prepareEmptyPacksView(model,useContinent,destinationLocation = Optional.of(destinationLocation))
        val packs = packService.packsSoldAt(useContinent,destinationLocation,archeageServer)

        return preparePacksView(packs,model,useContinent,Optional.of(destinationLocation), archeageServer = archeageServer)
    }

    private fun preparePacksView(packs: List<PackDTO>, model: Model, continent: Continent, destinationLocation: Optional<Long> = Optional.empty(), departureLocation: Optional<Long> = Optional.empty(),archeageServer: ArcheageServer) : String{
        model.addAttribute(
            "locations",
            locationService.continentLocations(continent,archeageServer).filter { location -> destinationLocation.isEmpty || (destinationLocation.isPresent && destinationLocation.get() != location.id) }
        )
        model.addAttribute(
            "factories",
            locationService.continentFactories(continent,archeageServer).filter { location -> departureLocation.isEmpty || (departureLocation.isPresent && departureLocation.get() != location.id) }
        )
        model.addAttribute("packs", packs)
        model.addAttribute("selectedContinent",continent)
        model.addAttribute("departureLocation",departureLocation)
        model.addAttribute("destinationLocation",destinationLocation)
        model.addAttribute("materials",packs.materialsWithPrice())

        return "packs"
    }

    private fun prepareEmptyPacksView(model: Model, continent: Continent, destinationLocation: Optional<Long> = Optional.empty(), departureLocation: Optional<Long> = Optional.empty()) : String{
        model.addAttribute("locations", emptyList<Location>())
        model.addAttribute("factories", emptyList<Location>())
        model.addAttribute("packs", emptyList<PackDTO>())
        model.addAttribute("selectedContinent",continent)
        model.addAttribute("departureLocation",departureLocation)
        model.addAttribute("destinationLocation",destinationLocation)
        model.addAttribute("materials", emptyList<CraftingMaterialDTO>())

        return "packs"
    }
}