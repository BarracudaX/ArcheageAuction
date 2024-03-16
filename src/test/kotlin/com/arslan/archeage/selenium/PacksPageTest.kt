package com.arslan.archeage.selenium

import com.arslan.archeage.Continent
import com.arslan.archeage.pageobjects.PacksPageObject
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.*
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder
import sortByWorkingPointsProfitAsc
import sortByWorkingPointsProfitDesc

class PacksPageTest : AbstractPacksPageTest() {

    private lateinit var page: PacksPageObject

    @BeforeEach
    override fun setUp() {
        super.setUp()
        page = PacksPageObject(webDriver, port, packService, retryTemplate).get()
    }

    @Test
    fun `should display error when user is accessing packs view page and user has not selected archeage server`() {
        page.error { this.shouldBe(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(), LocaleContextHolder.getLocale())) }
    }

    @Test
    fun `should not display error after user has selected archeage server`() {
        page
            .error { this.shouldNotBe(null) }
            .selectServer(archeageServer)
            .error { this.shouldBe(null) }
    }

    @Test
    fun `should display packs of the selected archeage server sorted by profit`() {
        page
            .selectServer(archeageServer)
            .packs{ actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,page.currentPageSize())) }
            .selectServer(anotherArcheageServer)
            .packs { actualPacks -> actualPacks.shouldContainExactly(anotherArcheageServerEastPack) }
    }

    @Test
    fun `should display packs that belong to selected departure location`() {
        page
            .selectServer(archeageServer)
            .selectDepartureLocation(eastDepartureLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastDepartureLocationPack) }
    }

    @Test
    fun `should display packs that belong to selected destination location`() {
        page
            .selectServer(archeageServer)
            .selectDestinationLocation(eastDestinationLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastDestinationLocationPack) }
    }

    @Test
    fun `should display packs that belong to selected category`() {
        page
            .selectServer(archeageServer)
            .selectCategory(anotherCategory)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastOtherCategoryPack) }
    }

    @Test
    fun `should display packs that belong to selected continent`() {
        page
            .selectServer(archeageServer)
            .selectContinent(Continent.WEST)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerWestPack) }
    }

    @Test
    fun `should not allow user to change pack percentage when user is not authenticated`() {
        page
            .selectServer(archeageServer)
            .packs { actualPacks ->
                actualPacks.forEach { pack -> shouldThrow<UnsupportedOperationException> { page.changePercentage(pack.id,100) } }
            }
    }

    @Test
    fun `should show more packs if more page size is increased`() {
        page
            .selectServer(archeageServer)
            .packs { actualPacks ->
                actualPacks shouldHaveSize page.currentPageSize()
                actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10)
            }
            .changePageSize(25)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,25)) }
    }

    @Test
    fun `should show less packs if page size is decreased`() {
        page
            .selectServer(archeageServer)
            .changePageSize(25)
            .changePageSize(10)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,10)) }
    }

    @Test
    fun `should show next packs when user requests next packs`() {
        page
            .selectServer(archeageServer)
            .nextPage()
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(10,20) }
    }

    @Test
    fun `should show previous packs when user requests previous packs`() {
        page
            .selectServer(archeageServer)
            .nextPage()
            .previousPage()
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10) }
    }

    @Test
    fun `should show packs of selected page when users requests it`() {
        page
            .selectServer(archeageServer)
            .selectPage(2)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(10,20) }
    }

    @Test
    fun `should show last packs when user requests it`() {
        page
            .selectServer(archeageServer)
        val expectedPacks = archeageServerEastPacks.drop(page.currentPageSize()*(page.paginationData().paginationNums.maxOfOrNull { it.content.toInt() }!! -1))
        page
            .lastPage()
            .packs { actualPacks -> actualPacks shouldContainExactly expectedPacks }
    }

    @Test
    fun `should show first packs when user requests it`() {
        page
            .selectServer(archeageServer)
            .selectPage(3)
            .firstPage()
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10) }
    }


    @Test
    fun `should allow user to sort packs by working points profit in descending order`() {
        page
            .selectServer(archeageServer)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10) }
            .sortByWorkingPointsProfitDesc()
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.sortByWorkingPointsProfitDesc().subList(0,10) }
    }

    @Test
    fun `should allow user to sort packs by working points profit in ascending order`() {
        page
            .selectServer(archeageServer)
            .sortByWorkingPointsProfitAsc()
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.sortByWorkingPointsProfitAsc().subList(0,10) }
    }
}