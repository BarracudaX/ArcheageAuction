package com.arslan.archeage.selenium

import com.arslan.archeage.UserNotAuthenticatedException
import com.arslan.archeage.pageobjects.HomePageObject
import com.arslan.archeage.pageobjects.LoginPageObject
import com.arslan.archeage.pageobjects.NavigationPageComponent
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class LoginPageTest : SeleniumTest() {

    private lateinit var loginPage: LoginPageObject
    private lateinit var homePage: HomePageObject
    private lateinit var navigation: NavigationPageComponent

    @BeforeEach
    override fun setUp() {
        navigation = NavigationPageComponent(webDriver,port)
        homePage = HomePageObject(webDriver,navigation,port)
        loginPage = LoginPageObject(webDriver,navigation,port)
        homePage.get()
    }

    @Test
    fun `should allow user to login using credentials`() {
        navigation.isProfileAvailable() shouldBe false
        loginPage.get()

        loginPage.login("test@email.com","TestPass123!")

        loginPage.isLoginSuccessful() shouldBe true
        loginPage.getErrorMessage() shouldBe null
        navigation.isProfileAvailable() shouldBe true
    }

    @Test
    fun `should show error if login fails`() {
        navigation.isProfileAvailable() shouldBe false
        loginPage.get()

        loginPage.login("invalid@email.com","credentials")

        loginPage.isLoginSuccessful() shouldBe false
        loginPage.getErrorMessage() shouldBe messageSource.getMessage("page.invalid.credentials.error", emptyArray(),LocaleContextHolder.getLocale())
        navigation.isProfileAvailable() shouldBe false
    }
}