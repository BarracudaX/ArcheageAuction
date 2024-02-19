package com.arslan.archeage.pageobjects

import io.kotest.matchers.shouldBe
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.LoadableComponent
import java.util.List

// page_url = http://localhost:8080/
class ProfilePageObject(private val driver: WebDriver,private val navigation: NavigationPageComponent,private val port: Int) : LoadableComponent<ProfilePageObject>(){

    override fun load() {
        driver.get("http://localhost:${port}/profile")
        PageFactory.initElements(driver, this)
    }

    override fun isLoaded() {
        driver.title shouldBe "My Profile"
    }
}