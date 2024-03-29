package com.arslan.archeage.controllers

import com.arslan.archeage.*
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.UserService
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.MultiValueMap
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/user")
class UserController(private val userService: UserService,private val itemPriceService: ItemPriceService, private val messageSource: MessageSource) {

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun register(@Validated registrationForm: RegistrationForm, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) return "register"

        try {
            userService.register(registrationForm)
            model.addAttribute("success", messageSource.getMessage("successful.registration.message", emptyArray(), LocaleContextHolder.getLocale()))
        } catch (ex: DataIntegrityViolationException) {
            bindingResult.rejectValue("email", "RegistrationForm.duplicate.email.message")
        }
        return "register"
    }

    @ResponseBody
    @GetMapping("/price")
    fun userPrices(@RequestParam params: MultiValueMap<String,String>, archeageServer: ArcheageServer,@AuthenticationPrincipal authentication: Long) : ResponseEntity<PricesDataTableResponse>{
        val pageable = params.pricesPageable()

        return ResponseEntity.ok(itemPriceService.userPrices(authentication,archeageServer,pageable).toDataTableResponse(params))
    }

    @ResponseBody
    @PostMapping("/price",consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun userPrice(@RequestBody price: UserPriceDTO,@AuthenticationPrincipal authentication: Long) : ResponseEntity<String>{
        itemPriceService.saveUserPrice(price.copy(userID = authentication))

        return ResponseEntity.ok(messageSource.getMessage("price.save.success", emptyArray(),LocaleContextHolder.getLocale()))
    }
}

@EqualPasswords(message = "{RegistrationForm.EqualPasswords.message}")
data class RegistrationForm(
    @field:Email(message = "{RegistrationForm.Email.email.message}")
    @field:NotBlank(message = "{RegistrationForm.NotBlank.email.message}")
    var email: String = "",

    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}\$", message = "{RegistrationForm.Pattern.password.message}")
    var password: String = "",

    var repeatedPassword: String = ""
)