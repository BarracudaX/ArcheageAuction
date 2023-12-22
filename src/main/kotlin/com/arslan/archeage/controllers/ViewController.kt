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
class ViewController {

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

    @GetMapping("/packs_view")
    fun allPacksView(model: Model,continent: Optional<Continent>) : String{
        model.addAttribute(continent.getOrElse { Continent.values()[0] })
        return "packs"
    }

}