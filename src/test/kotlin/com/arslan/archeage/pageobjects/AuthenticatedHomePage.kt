package com.arslan.archeage.pageobjects

import org.openqa.selenium.WebDriver

class AuthenticatedHomePage(driver: WebDriver, port: Int) : AbstractAuthenticatedPageObject<AuthenticatedHomePage>(driver, port) {
    override fun isSubclassLoaded() {
    }

    override fun load() {
        driver.get("http://localhost:${port}/")
    }
}