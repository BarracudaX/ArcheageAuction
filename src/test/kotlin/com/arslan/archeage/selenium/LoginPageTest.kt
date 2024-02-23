package com.arslan.archeage.selenium

import com.arslan.archeage.pageobjects.HomePageObject
import com.arslan.archeage.pageobjects.LoginPageObject
import io.kotest.assertions.shouldFail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class LoginPageTest : SeleniumTest() {

    private lateinit var loginPage: LoginPageObject
    private lateinit var homePage: HomePageObject

    @BeforeEach
    override fun setUp() {
        homePage = HomePageObject(webDriver, port)
        loginPage = LoginPageObject(webDriver, port)
        homePage.get()
        loginPage.get()
    }

    @Test
    fun `should allow user to login using credentials`() {
        val email = "test@email.com"
        val password = "TestPass123!"
        createUser(email, password)

        loginPage.login(email, password)
    }

    @Test
    fun `should show error if login fails`() {
        loginPage.invalidLogin("invalid@email.com","credentials")

        loginPage.getErrorMessage() shouldBe messageSource.getMessage("page.invalid.credentials.error", emptyArray(),LocaleContextHolder.getLocale())
    }
}