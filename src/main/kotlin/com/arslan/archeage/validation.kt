package com.arslan.archeage

import com.arslan.archeage.controllers.RegistrationForm
import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Constraint(validatedBy = [EqualPasswordsConstraintValidator::class])
annotation class EqualPasswords(val message: String,val groups: Array<KClass<*>> = [],val payload: Array<KClass<out Payload>> = [])

@Component
class EqualPasswordsConstraintValidator : ConstraintValidator<EqualPasswords,RegistrationForm>{
    override fun isValid(value: RegistrationForm?, context: ConstraintValidatorContext): Boolean {
        if(value == null) return true

        return value.password == value.repeatedPassword
    }

}