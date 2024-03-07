package com.arslan.archeage.pageobjects

import io.kotest.assertions.fail
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.LoadableComponent

abstract class AbstractUnauthenticatedPageObject<T : AbstractUnauthenticatedPageObject<T>>(private val driver: WebDriver,private val port: Int) : LoadableComponent<T>(){

    private val englishLanguage = By.xpath("//*[text() = 'English']")

    private val russianLanguageSelector = By.xpath("//*[text() = 'Русский']")

    private val loginSelector = By.xpath("//*[text() = 'Login']")

    private val registerSelector = By.xpath("//*[text() = 'Register']")

    private val packagesSelector = By.xpath("//*[text() = 'Packages']")

    fun toLoginPage() : LoginPageObject {
        driver.findElement(loginSelector).click()

        return LoginPageObject(driver, port).get()
    }

    final override fun isLoaded() {
        try{
            driver.findElement(registerSelector)
            isSubclassLoaded()
        }catch (_: NoSuchElementException){
            fail("Navigation does not contain link for registration page.")
        }
    }

    protected abstract fun isSubclassLoaded()

}