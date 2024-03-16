package com.arslan.archeage.pageobjects

import io.kotest.matchers.shouldBe
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.LoadableComponent

// page_url = http://localhost:8080/login
class LoginPageObject(private val driver: WebDriver, private val port: Int) : LoadableComponent<LoginPageObject>(){

    private val inputEmail = By.id("inputEmail")

    private val inputPassword = By.id("inputPassword")

    private val loginButton= By.cssSelector("button[class$='btn-success']")

    private val errorAlert = By.cssSelector("div.alert.alert-danger")

    private val url = "http://localhost:${port}/login"
    override fun isLoaded() {
        driver.title shouldBe "Login"
    }

    override fun load() {
        driver.get("http://localhost:${port}/login")
    }

    fun login(authenticationData: AuthenticationData) : AuthenticatedHomePage{
        performLogin(authenticationData.email, authenticationData.password)

        return AuthenticatedHomePage(driver,port).get()
    }

    fun invalidLogin(email: String,password: String){
        performLogin(email,password)
        driver.title shouldBe "Login"
    }

    private fun performLogin(email: String, password: String) {
        driver.findElement(inputEmail).sendKeys(email)
        driver.findElement(inputPassword).sendKeys(password)
        driver.findElement(loginButton).click()
    }

    fun getErrorMessage() : String?{
        return try{
            driver.findElement(errorAlert).text
        }catch (_: NoSuchElementException){
            null
        }
    }

}
data class AuthenticationData(val email: String,val password: String,val userID: Long)