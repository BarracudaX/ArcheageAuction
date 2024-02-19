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
class LoginPageObject(private val driver: WebDriver, private val navigation: NavigationPageComponent, private val port: Int) : LoadableComponent<LoginPageObject>(){

    private val inputEmail = By.id("inputEmail")

    private val inputPassword = By.id("inputPassword")

    private val loginButton= By.cssSelector("button[class$='btn-success']")

    private val errorAlert = By.cssSelector("div.alert.alert-danger")

    private val url = "http://localhost:${port}/login"

    override fun load() {
        driver.get("http://localhost:${port}/login")
        PageFactory.initElements(driver,this)
    }

    override fun isLoaded() {
        driver.title shouldBe "Login"
    }

    fun login(email: String,password: String) : LoginPageObject{
        driver.findElement(inputEmail).sendKeys(email)
        driver.findElement(inputPassword).sendKeys(password)
        driver.findElement(loginButton).click()

        return this
    }

    fun isLoginSuccessful() : Boolean = !driver.currentUrl.contains("/login")

    fun getErrorMessage() : String?{
        return try{
            driver.findElement(errorAlert).text
        }catch (_: NoSuchElementException){
            null
        }
    }
}