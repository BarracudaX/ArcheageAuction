package com.arslan.archeage.controllers

import com.arslan.archeage.EqualPasswords
import com.arslan.archeage.service.UserService
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.UnsupportedOperationException

@Controller
@RequestMapping("/register")
class RegisterController(private val userService: UserService,private val messageSource: MessageSource) {

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun register(@Validated registrationForm: RegistrationForm,bindingResult: BindingResult,model: Model) : String{
        if(bindingResult.hasErrors()) return "register"

        try{
            userService.register(registrationForm)
            model.addAttribute("success",messageSource.getMessage("successful.registration.message", emptyArray(),LocaleContextHolder.getLocale()))
        }catch (ex: DataIntegrityViolationException){
            bindingResult.rejectValue("email","RegistrationForm.duplicate.email.message")
        }
        return "register"
    }
}

@EqualPasswords(message = "{RegistrationForm.EqualPasswords.message}")
class RegistrationForm{

    @field:Email(message = "{RegistrationForm.Email.email.message}")
    var email: String = ""

    @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}\$", message = "{RegistrationForm.Pattern.password.message}")
    var password: String = ""

    var repeatedPassword: String = ""
}