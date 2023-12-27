package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.*
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.util.Optional
import kotlin.jvm.optionals.getOrElse

@RequestMapping("/",produces = [MediaType.TEXT_HTML_VALUE])
@Controller
class ViewController {

    companion object{
        const val LOGIN_PAGE_VIEW = "login"
        const val PROFILE_PAGE_VIEW = "profile"
        const val INDEX_PAGE_VIEW = "index"
        const val REGISTER_PAGE_VIEW = "register"
        const val PACKS_PAGE_VIEW = "packs"
    }

    @GetMapping("/login")
    fun loginPage() : String = LOGIN_PAGE_VIEW


    @GetMapping("/profile")
    fun profile(pageable: Pageable, model: Model) : String = PROFILE_PAGE_VIEW

    @GetMapping
    fun homePage(): String = INDEX_PAGE_VIEW

    @GetMapping("/register")
    fun registerPage(model: Model) : String {
        model.addAttribute("registrationForm",RegistrationForm())
        return REGISTER_PAGE_VIEW
    }

    @GetMapping("/packs_view")
    fun allPacksView() : String = PACKS_PAGE_VIEW

}