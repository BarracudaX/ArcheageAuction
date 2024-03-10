package com.arslan.archeage.selenium

import com.arslan.archeage.Continent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.*
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class PackagesPageTest : AbstractPackagesPageTest() {

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
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .packs{ actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,page.currentPageSize())) }
            .selectServer(anotherArcheageServer,anotherArcheageServerEastPacks[0].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(anotherArcheageServerEastPacks.subList(0,page.currentPageSize())) }
    }

    @Test
    fun `should display packs that belong to selected departure location`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .selectDepartureLocation(eastDepartureLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastDepartureLocationPacks.subList(0,10)) }
            .selectDepartureLocation(anotherEastDepartureLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastOtherDepartureLocationPacks.subList(0,10)) }
    }

    @Test
    fun `should display packs that belong to selected destination location`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .selectDestinationLocation(eastDestinationLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerDestinationLocationPacks.subList(0,10)) }
            .selectDestinationLocation(anotherEastDestinationLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerOtherDestinationLocationPacks.subList(0,10)) }
    }

    @Test
    fun `should display packs that belong to selected category`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .selectCategory(category,archeageServerEastCategoryPacks[0].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastCategoryPacks.subList(0,10)) }
            .deselectCategory(category,archeageServerEastPacks[0].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,10)) }
            .selectCategory(anotherCategory,archeageServerEastOtherCategoryPacks[0].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastOtherCategoryPacks.subList(0,10)) }
    }

    @Test
    fun `should display packs that belong to selected continent`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .selectContinent(Continent.WEST,archeageServerWestPacks[0].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerWestPacks.subList(0,10)) }
            .selectContinent(Continent.EAST,archeageServerEastPacks[0].id)
            .packs{ actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,10)) }
    }

    @Test
    fun `should not allow user to change pack percentage when user is not authenticated`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .packs { actualPacks ->
                actualPacks.forEach { pack -> shouldThrow<UnsupportedOperationException> { page.changePercentage(pack,100) } }
            }
    }

    @Test
    fun `should show more packs if more page size is increased`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .packs { actualPacks ->
                actualPacks shouldHaveSize page.currentPageSize()
                actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10)
            }
            .changePageSize(25,archeageServerEastPacks[24].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,25)) }
    }

    @Test
    fun `should show less packs if page size is decreased`() {
        page
            .selectServer(archeageServer, archeageServerEastPacks[0].id)
            .changePageSize(25, archeageServerEastPacks[24].id)
            .changePageSize(10, archeageServerEastPacks[0].id,archeageServerEastPacks[24].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(archeageServerEastPacks.subList(0,10)) }
    }

    @Test
    fun `should show next packs when user requests next packs`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .nextPage(archeageServerEastPacks[10].id)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(10,20) }
    }

    @Test
    fun `should show previous packs when user requests previous packs`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .nextPage(archeageServerEastPacks[10].id)
            .previousPage(archeageServerEastPacks[0].id)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10) }
    }

    @Test
    fun `should show packs of selected page when users requests it`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .selectPage(2,archeageServerEastPacks[10].id)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(10,20) }
    }

    @Test
    fun `should show last packs when user requests it`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .lastPage(archeageServerEastPacks.last().id)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.takeLast(10) }
    }

    @Test
    fun `should show first packs when user requests it`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .selectPage(3,archeageServerEastPacks[25].id)
            .firstPage(archeageServerEastPacks[0].id)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10) }
    }


    @Test
    fun `should allow user to sort packs by working points profit`() {
        page
            .selectServer(archeageServer,archeageServerEastPacks[0].id)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.subList(0,10) }
            .sortByWorkingPointsProfitDesc(archeageServerEastPacks.sortByWorkingPointsProfit()[0].id)
            .packs { actualPacks -> actualPacks shouldContainExactly archeageServerEastPacks.sortByWorkingPointsProfit().subList(0,10) }
    }
}