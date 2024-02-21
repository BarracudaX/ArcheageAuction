package com.arslan.archeage.pageobjects

import com.arslan.archeage.controllers.RegistrationForm
import com.arslan.archeage.pageobjects.RegistrationResult.*
import io.kotest.matchers.shouldBe
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.LoadableComponent

// page_url = http://localhost:8080/register
class RegisterPage(private val driver: WebDriver,private val port: Int) : AbstractUnauthenticatedPageObject<RegisterPage>(driver,port) {
    
    private val inputEmail = By.id("inputEmail")

    private val inputPassword = By.id("inputPassword")
    
    private val repeatedPassword = By.name("repeatedPassword")

    private val registerButton = By.cssSelector("button[class\$='btn-success']")

    private val errorSpan = By.cssSelector("div[class*='alert-danger'] span")

    private val successDiv = By.cssSelector("div[class*='alert-success']")

    fun register(form: RegistrationForm) : RegistrationResult{
        driver.findElement(inputEmail).sendKeys(form.email)
        driver.findElement(inputPassword).sendKeys(form.password)
        driver.findElement(repeatedPassword).sendKeys(form.repeatedPassword)
        driver.findElement(registerButton).click()
        
        val errors = driver.findElements(errorSpan).map{ it.text }

        return if(errors.isEmpty()){
            SuccessfulRegistration(driver.findElement(successDiv).text)
        }else{
            RegistrationFailure(errors)
        }
    }

    override fun load() {
        driver.get("http://localhost:${port}/register")
    }

    override fun isSubclassLoaded() {
        driver.title shouldBe "Register"
    }


}

sealed class RegistrationResult{

    data class SuccessfulRegistration(val successMessage: String) : RegistrationResult()

    data class RegistrationFailure(val errors: List<String>) : RegistrationResult()

}