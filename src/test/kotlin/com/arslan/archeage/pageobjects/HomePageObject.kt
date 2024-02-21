package com.arslan.archeage.pageobjects

import io.kotest.matchers.shouldBe
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.LoadableComponent

// page_url = http://localhost:8080/
class HomePageObject(private val driver: WebDriver, private val port: Int) :  AbstractUnauthenticatedPageObject<HomePageObject>(driver,port) {

    override fun load() {
        driver.get("http://localhost:${port}")
        PageFactory.initElements(driver, this)
    }

    override fun isSubclassLoaded() {
        driver.title shouldBe "Archeage Auction"
    }


}