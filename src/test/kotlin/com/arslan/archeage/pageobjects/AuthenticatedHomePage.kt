package com.arslan.archeage.pageobjects

import io.kotest.assertions.fail
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.LoadableComponent

class AuthenticatedHomePage(private val driver: WebDriver, private val port: Int) : LoadableComponent<AuthenticatedHomePage>() {

    private val logout = By.ByXPath("//*[text() = 'Logout']")

    override fun isLoaded() {
        try{
            driver.findElement(logout)
        }catch (_: NoSuchElementException){
            fail("Missing logout. Probably failed authentication.")
        }

    }

    override fun load() {
        driver.get("http://localhost:${port}/")
    }
}