package com.arslan.archeage.pageobjects

import com.arslan.archeage.UserNotAuthenticatedException
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.LoadableComponent


class NavigationPageComponent(private val driver: WebDriver,private val port: Int) : LoadableComponent<NavigationPageComponent>(){

    private val englishLanguage = By.xpath("//*[text() = 'English']")

    private val russianLanguageSelector = By.xpath("//*[text() = 'Русский']")

    private val nagasharServer = By.xpath("//*[text() = 'нагашар']")

    private val loginSelector = By.xpath("//*[text() = 'Login']")

    private val registerSelector = By.xpath("//*[text() = 'Register']")

    private val packagesSelector = By.xpath("//*[text() = 'Packages']")

    private val profileSelector = By.xpath("//*[text() = 'My Profile']")

    fun toLoginPage() : LoginPageObject {
        driver.findElement(loginSelector).click()

        return LoginPageObject(driver,this,port).get()
    }

    fun toProfilePage() : ProfilePageObject {
        return try{
            driver.findElement(profileSelector)
             ProfilePageObject(driver,this,port)
        }catch (ex: NoSuchElementException){
            throw UserNotAuthenticatedException()
        }
    }

    fun isProfileAvailable() : Boolean = try{
        driver.findElement(profileSelector)
        true
    }catch (ex: NoSuchElementException){
        false
    }

    override fun load() {
        PageFactory.initElements(driver,this)
    }

    override fun isLoaded() {
        driver.findElements(By.className("nav-link")) shouldHaveAtLeastSize 2
    }
}