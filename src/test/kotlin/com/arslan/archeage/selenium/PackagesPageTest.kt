package com.arslan.archeage.selenium

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Category
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.pageobjects.PackagesPageObject
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder
import java.lang.UnsupportedOperationException

class PackagesPageTest : SeleniumTest() {

    private lateinit var page: PackagesPageObject
    private lateinit var archeageServer: ArcheageServer
    private lateinit var anotherArcheageServer: ArcheageServer
    private val packs = mutableListOf<PackDTO>()
    private lateinit var westContinentPack: PackDTO
    private val anotherArcheageServerPacks = mutableListOf<PackDTO>()
    private lateinit var departureLocation: Location
    private lateinit var anotherDepartureLocation: Location
    private lateinit var anotherArcheageServerDepartureLocation: Location
    private lateinit var destinationLocation: Location
    private lateinit var anotherDestinationLocation: Location
    private lateinit var anotherArcheageServerDestinationLocation: Location
    private lateinit var category: Category
    private lateinit var anotherCategory: Category
    private lateinit var anotherArcheageServerCategory: Category

    @BeforeEach
    override fun setUp() {
        super.setUp()
        archeageServer = createArcheageServer("SOME_ARCHEAGE_SERVER")
        anotherArcheageServer = createArcheageServer("ANOTHER_ARCHEAGE_SERVER")

        departureLocation = createEastDepartureLocation("SOME_EAST_CREATE_LOCATION_1",archeageServer)
        anotherDepartureLocation = createEastDepartureLocation("SOME_EAST_CREATE_LOCATION_2",archeageServer)
        anotherArcheageServerDepartureLocation = createEastDepartureLocation("ANOTHER_SERVER_EAST_LOCATION",anotherArcheageServer)

        destinationLocation = createEastDestinationLocation("SOME_EAST_SELL_LOCATION_1",archeageServer)
        anotherDestinationLocation = createEastDestinationLocation("SOME_EAST_SELL_LOCATION_2",archeageServer)
        anotherArcheageServerDestinationLocation = createEastDestinationLocation("ANOTHER_SERVER_EAST_SELL_LOCATION",anotherArcheageServer)

        category = createCategory("SOME_CATEGORY_1",archeageServer)
        anotherCategory = createCategory("SOME_CATEGORY_2",archeageServer)
        anotherArcheageServerCategory = createCategory("ANOTHER_ARCHEAGE_SERVER_CATEGORY",anotherArcheageServer)

        packs.add(createPack("SOME_PACK_1", departureLocation, destinationLocation, Price.of(50,0,0), 10, category, listOf(Triple("M1",Price(5,0,0),5))))
        packs.add(createPack("SOME_PACK_2",anotherDepartureLocation,anotherDestinationLocation,Price.of(50,0,0),5,anotherCategory, listOf(Triple("M2",Price(1,0,0),10),Triple("M3",Price(1,0,0),5))))
        anotherArcheageServerPacks.add(createPack("ANOTHER_PACK_1",anotherArcheageServerDepartureLocation,anotherArcheageServerDestinationLocation,Price.of(10,0,0),5,anotherArcheageServerCategory,listOf(Triple("M3",Price.of(1,0,0),1))))
        westContinentPack = createPack("WEST_PACK",createWestDepartureLocation("WEST_LOCATION",archeageServer),createWestDestinationLocation("WEST_SELL_LOCATION",archeageServer), Price.of(1,1,1),1,category,listOf(Triple("M4", Price.of(0,0,1),1)))

        page = PackagesPageObject(webDriver,port).get()
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
            .selectServer(archeageServer,packs[0].id)
            .packs{ actualPacks -> actualPacks.shouldContainExactly(packs.sortedByDescending { it.profit }) }
            .selectServer(anotherArcheageServer,anotherArcheageServerPacks[0].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(anotherArcheageServerPacks.sortedByDescending { it.profit }) }
    }

    @Test
    fun `should display packs that belong to selected departure location`() {
        page
            .selectServer(archeageServer,packs.sortedByDescending(PackDTO::profit)[0].id)
            .selectDepartureLocation(departureLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(packs[0]) }
            .selectDepartureLocation(anotherDepartureLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(packs[1]) }
    }

    @Test
    fun `should display packs that belong to selected destination location`() {
        page
            .selectServer(archeageServer,packs.sortedByDescending(PackDTO::profit)[0].id)
            .selectDestinationLocation(destinationLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(packs[0]) }
            .selectDestinationLocation(anotherDestinationLocation)
            .packs { actualPacks -> actualPacks.shouldContainExactly(packs[1]) }
    }

    @Test
    fun `should display packs that belong to selected category`() {
        page
            .selectServer(archeageServer,packs.sortedByDescending(PackDTO::profit)[0].id)
            .selectCategory(category,packs[0].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(packs[0]) }
            .deselectCategory(category,packs[1].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(packs.sortedByDescending(PackDTO::profit)) }
            .selectCategory(anotherCategory,packs[1].id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(packs[1]) }
    }

    @Test
    fun `should display packs that belong to selected continent`() {
        page
            .selectServer(archeageServer,packs.sortedByDescending(PackDTO::profit)[0].id)
            .selectContinent(Continent.WEST,westContinentPack.id)
            .packs { actualPacks -> actualPacks.shouldContainExactly(westContinentPack) }
            .selectContinent(Continent.EAST,packs[0].id)
            .packs{ actualPacks -> actualPacks.shouldContainExactly(packs.sortedByDescending(PackDTO::profit)) }
    }

    @Test
    fun `should not allow user to change pack percentage when user is not authenticated`() {
        page
            .selectServer(archeageServer,packs.sortedByDescending(PackDTO::profit)[0].id)
            .packs { actualPacks ->
                actualPacks.forEach { pack -> shouldThrow<UnsupportedOperationException> { page.changePercentage(pack,100) } }
            }
    }
}