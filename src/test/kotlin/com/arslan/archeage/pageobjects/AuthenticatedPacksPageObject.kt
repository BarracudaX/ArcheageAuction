package com.arslan.archeage.pageobjects

import com.arslan.archeage.service.PackService
import org.openqa.selenium.WebDriver
import org.springframework.retry.support.RetryTemplate

class AuthenticatedPacksPageObject(private val authenticationData: AuthenticationData,driver: WebDriver, port: Int, packService: PackService, retryTemplate: RetryTemplate) : PacksPageObject(driver, port, packService, retryTemplate) {

    private val loginPage = LoginPageObject(driver,port)

    override fun load() {
        loginPage.get().login(authenticationData)
        driver.get("http://localhost:${port}/packs_view")
    }

    override fun userID(): Long = authenticationData.userID
}