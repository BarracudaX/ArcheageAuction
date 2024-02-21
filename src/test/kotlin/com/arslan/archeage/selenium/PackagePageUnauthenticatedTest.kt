package com.arslan.archeage.selenium

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.pageobjects.PackagesPageObject
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class PackagePageUnauthenticatedTest : SeleniumTest(){

    private lateinit var page: PackagesPageObject
    private lateinit var archeageServer: ArcheageServer
    private lateinit var location: Location
    @BeforeEach
    override fun setUp() {
        super.setUp()
        archeageServer = createArcheageServer()
        location = createWestLocation(archeageServer)
        page = PackagesPageObject(webDriver,port)
    }

    @Test
    fun `should display error when user is accessing packs view page and user has not selected archeage server`() {
        page.get()

        page.error() shouldBe messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),LocaleContextHolder.getLocale())
    }

    @Disabled("TODO")
    @Test
    fun `should not display error after user has selected archeage server`() {
        page.get()

        page.selectServer(archeageServer)

        page.error() shouldBe null
    }
}