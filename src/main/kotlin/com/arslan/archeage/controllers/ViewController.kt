package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.service.LocationService
import com.arslan.archeage.service.PackService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/")
@Controller
class ViewController(private val locationService: LocationService, private val packService: PackService) {

    @GetMapping
    fun homePage() : String = "index"

    @GetMapping("/packs")
    fun packsPage(model: Model) : String {
        val continent = Continent.values()[0]
        model.addAttribute("locations",locationService.continentLocations(continent))
        model.addAttribute("factories",locationService.continentFactories(continent))
        model.addAttribute("packs",packService.packs(continent))
        return "packs"
    }
}