package com.arslan.archeage.pageobjects

import io.kotest.matchers.shouldBe
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.LoadableComponent

class UserProfilePageObject(private val driver: WebDriver,private val port: Int,private val authenticationData: AuthenticationData) : LoadableComponent<UserProfilePageObject>() {

    private val loginPage = LoginPageObject(driver,port)

    override fun get(): UserProfilePageObject {
        loginPage.get().login(authenticationData)
        try {
            isLoaded()
            return this
        } catch (e: Error) {
            load()
        }

        isLoaded()

        return this
    }

    override fun load() {
        driver.get("http://localhost:${port}/profile")
    }

    override fun isLoaded() {
        driver.title shouldBe "My Profile"
    }



}