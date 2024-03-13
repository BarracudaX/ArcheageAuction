package com.arslan.archeage.selenium

import capitalized
import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.pageobjects.PackagesPageObject
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class PackagesPageContinentTest : SeleniumTest(){

    private lateinit var page: PackagesPageObject
    private lateinit var archeageServer: ArcheageServer

    @BeforeEach
    override fun setUp() {
        super.setUp()
        archeageServer = createArcheageServer("SOME_ARCHEAGE_SERVER")
        page = PackagesPageObject(webDriver,port,packService,retryTemplate).get()
    }

    @Test
    fun `should display continents to the user`() {
        page.continents() shouldContainExactlyInAnyOrder Continent.entries.map { messageSource.getMessage("page.continent.${it.name}", emptyArray(),LocaleContextHolder.getLocale()) }
    }

    @Test
    fun `should select by default the first continent`() {
        page.selectedContinent() shouldBe Continent.entries[0]
    }


}