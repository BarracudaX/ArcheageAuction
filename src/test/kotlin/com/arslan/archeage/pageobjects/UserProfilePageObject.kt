package com.arslan.archeage.pageobjects

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.pageobjects.component.SelectComponent
import com.arslan.archeage.selectArcheageServer
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

    fun selectArcheageServer(archeageServer: ArcheageServer) : UserProfilePageObject{
        driver.selectArcheageServer(archeageServer)
        return this
    }




}