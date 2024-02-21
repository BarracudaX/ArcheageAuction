package com.arslan.archeage.pageobjects

import io.kotest.assertions.fail
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.LoadableComponent

abstract class AbstractAuthenticatedPageObject<T : AbstractAuthenticatedPageObject<T>>(protected val driver: WebDriver,protected val port: Int) : LoadableComponent<T>(){

    private val logout = By.ByXPath("//*[text() = 'Logout']")

    override fun isLoaded() {
        try{
            driver.findElement(logout)
            isSubclassLoaded()
        }catch (_: NoSuchElementException){
            fail("Missing logout. Probably failed authentication.")
        }

    }

    protected abstract fun isSubclassLoaded()
}