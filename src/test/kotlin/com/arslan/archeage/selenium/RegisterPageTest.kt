package com.arslan.archeage.selenium

import com.arslan.archeage.controllers.RegistrationForm
import com.arslan.archeage.entity.User
import com.arslan.archeage.entity.UserRole
import com.arslan.archeage.pageobjects.RegisterPage
import com.arslan.archeage.pageobjects.RegistrationResult
import com.arslan.archeage.pageobjects.RegistrationResult.RegistrationFailure
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.context.i18n.LocaleContextHolder

class RegisterPageTest : SeleniumTest(){

    private val form = RegistrationForm("someEmail@email.com","SomePass123","SomePass123")

    private lateinit var registerPage: RegisterPage

    @BeforeEach
    override fun setUp() {
        super.setUp()
        registerPage = RegisterPage(webDriver,port)
    }

    @MethodSource("invalidPasswords")
    @ParameterizedTest
    fun `should fail registration if password is invalid`(invalidPassword: String) {
        userRepository.findByEmail(form.email) shouldBe null

        val errors = (registerPage.get().register(form.copy(password = invalidPassword, repeatedPassword = invalidPassword)) as RegistrationFailure).errors

        errors
            .shouldHaveSize(1)
            .shouldContainExactly(messageSource.getMessage("RegistrationForm.Pattern.password.message", emptyArray(),LocaleContextHolder.getLocale()))
        userRepository.findByEmail(form.email) shouldBe null
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    fun `should fail registration if email is invalid`(invalidEmail: String) {
        userRepository.findByEmail(invalidEmail) shouldBe null

        val errors = (registerPage.get().register(form.copy(email = invalidEmail)) as RegistrationFailure).errors

        errors
            .shouldHaveSize(1)
            .shouldContainExactly(messageSource.getMessage("RegistrationForm.Email.email.message", emptyArray(),LocaleContextHolder.getLocale()))
        userRepository.findByEmail(invalidEmail) shouldBe null
    }

    @Test
    fun `should fail registration if email empty`() {
        userRepository.findByEmail("") shouldBe null

        val errors = (registerPage.get().register(form.copy(email = "")) as RegistrationFailure).errors

        errors
            .shouldHaveSize(1)
            .shouldContainExactly(messageSource.getMessage("RegistrationForm.NotBlank.email.message", emptyArray(),LocaleContextHolder.getLocale()))
        userRepository.findByEmail("") shouldBe null
    }

    @Test
    fun `should fail registration if passwords do not match`() {
        userRepository.findByEmail(form.email) shouldBe null

        val errors = (registerPage.get().register(form.copy(repeatedPassword = form.password.plus("1"))) as RegistrationFailure).errors

        errors
            .shouldHaveSize(1)
            .shouldContainExactly(messageSource.getMessage("RegistrationForm.EqualPasswords.message", emptyArray(),LocaleContextHolder.getLocale()))
        userRepository.findByEmail(form.email) shouldBe null
    }

    @Test
    fun `should fail registration if user with provided email already exists`() {
        val existingUser = userRepository.save(User("test@email.com",passwordEncoder.encode("AnyPass123")))

        val errors = (registerPage.get().register(form.copy(email = existingUser.email)) as RegistrationFailure).errors

        errors
            .shouldHaveSize(1)
            .shouldContainExactly(messageSource.getMessage("RegistrationForm.duplicate.email.message", emptyArray(),LocaleContextHolder.getLocale()))
        passwordEncoder.matches(form.password,userRepository.findByEmail(existingUser.email)!!.password) shouldBe false
    }

    @Test
    fun `should register new user`() {
        userRepository.findByEmail(form.email) shouldBe null

        val successfulRegistration  = registerPage.get().register(form) as RegistrationResult.SuccessfulRegistration

        successfulRegistration.successMessage shouldBe messageSource.getMessage("successful.registration.message", emptyArray(),LocaleContextHolder.getLocale())
        assertSoftly(userRepository.findByEmail(form.email)!!) {
            email shouldBe form.email
            role shouldBe UserRole.USER
            passwordEncoder.matches(form.password,password) shouldBe true
        }
    }
}