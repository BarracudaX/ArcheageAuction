package com.arslan.archeage.selenium

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.pageobjects.PackagesPageObject
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class PackagesPageTest : SeleniumTest() {

    private lateinit var page: PackagesPageObject
    private lateinit var archeageServer: ArcheageServer

    @BeforeEach
    override fun setUp() {
        super.setUp()
        archeageServer = createArcheageServer()
        page = PackagesPageObject(webDriver,port).get()
    }

    @Test
    fun `should display error when user is accessing packs view page and user has not selected archeage server`() {
        page
            .error { this.shouldBe(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),
                LocaleContextHolder.getLocale())) }

    }

    @Test
    fun `should not display error after user has selected archeage server`() {
        page
            .error { this.shouldNotBe(null) }
            .selectServer(archeageServer)
            .error { this.shouldBe(null) }
    }
}