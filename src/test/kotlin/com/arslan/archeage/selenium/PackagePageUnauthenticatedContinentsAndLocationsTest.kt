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
import kotlin.math.exp

class PackagePageUnauthenticatedContinentsAndLocationsTest : SeleniumTest(){

    private lateinit var page: PackagesPageObject
    private lateinit var archeageServer: ArcheageServer
    private val eastDepartureLocations = mutableListOf<Location>()
    private val eastDestinationLocations = mutableListOf<Location>()
    private val westDepartureLocations = mutableListOf<Location>()
    private val westDestinationLocations = mutableListOf<Location>()

    @BeforeEach
    override fun setUp() {
        super.setUp()
        archeageServer = createArcheageServer()
        eastDepartureLocations.add(createEastDepartureLocation("SOME_EAST_DEPARTURE_LOCATION_1",archeageServer))
        eastDepartureLocations.add(createEastDepartureLocation("SOME_EAST_DEPARTURE_LOCATION_2",archeageServer))
        westDepartureLocations.add(createWestDepartureLocation("SOME_WEST_DEPARTURE_LOCATION_1",archeageServer))
        westDepartureLocations.add(createWestDepartureLocation("SOME_WEST_DEPARTURE_LOCATION_2",archeageServer))
        westDestinationLocations.add(createWestDestinationLocation("SOME_WEST_DESTINATION_LOCATION_1",archeageServer))
        westDestinationLocations.add(createWestDestinationLocation("SOME_WEST_DESTINATION_LOCATION_2",archeageServer))
        eastDestinationLocations.add(createEastDestinationLocation("SOME_EAST_DESTINATION_LOCATION_1",archeageServer))
        eastDestinationLocations.add(createEastDestinationLocation("SOME_EAST_DESTINATION_LOCATION_2",archeageServer))
        page = PackagesPageObject(webDriver,port)
    }

    @Test
    fun `should display error when user is accessing packs view page and user has not selected archeage server`() {
        page
            .get()
            .error { this.shouldBe(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),LocaleContextHolder.getLocale())) }

    }

    @Test
    fun `should not display error after user has selected archeage server`() {
        page
            .get()
            .error { this.shouldNotBe(null) }
            .selectServer(archeageServer)
            .error { this.shouldBe(null) }
    }

    @Test
    fun `should display continents to the user`() {
        page
            .get()
            .continents() shouldContainExactlyInAnyOrder Continent.entries.map { messageSource.getMessage("page.continent.${it.name}", emptyArray(),LocaleContextHolder.getLocale()) }
    }

    @Test
    fun `should select by default the first continent`() {
        page
            .get()
            .selectedContinent()shouldBe messageSource.getMessage("page.continent.${Continent.entries[0].name}", emptyArray(),LocaleContextHolder.getLocale())
    }

    @Test
    fun `should display departure locations of selected continent`() {
        page
            .get()
            .selectServer(archeageServer)
            .departureLocations{ this.shouldContainExactlyInAnyOrder(getExpectedDepartureLocations(Continent.EAST)) }
            .selectContinent(Continent.WEST,westDepartureLocations[0])
            .departureLocations{ this.shouldContainExactlyInAnyOrder(getExpectedDepartureLocations(Continent.WEST)) }
    }

    @Test
    fun `should display destination locations of selected continent`() {
        page
            .get()
            .selectServer(archeageServer)
            .destinationLocations{ this.shouldContainExactlyInAnyOrder(getExpectedDestinationLocations(Continent.EAST)) }
            .selectContinent(Continent.WEST,westDestinationLocations[0])
            .destinationLocations{ this.shouldContainExactlyInAnyOrder(getExpectedDestinationLocations(Continent.WEST)) }
    }

    @Test
    fun `should display empty destination and departure locations when archeage server is not selected`() {
        page
            .get()
            .destinationLocations{ this.shouldHaveSize(0) }
            .departureLocations{ this.shouldHaveSize(0) }
    }

    @Test
    fun `should not display destination location if it is selected as departure location`() {
        page
            .get()
            .selectServer(archeageServer)
            .destinationLocations{ this.shouldContain(eastDestinationLocations[0].toDisplayedValue())  }
            .selectDepartureLocation(eastDestinationLocations[0])
            .destinationLocations{ this.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
    }

    @Test
    fun `should not display departure location if it is selected as destination location`() {
        page
            .get()
            .selectServer(archeageServer)
            .departureLocations{ this.shouldContain(eastDestinationLocations[0].toDisplayedValue()) }
            .selectDestinationLocation(eastDestinationLocations[0])
            .departureLocations{ this.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
    }

    @Test
    fun `should display destination location back when different departure location is selected`() {
        page.get()
            .selectServer(archeageServer)
            .selectDepartureLocation(eastDestinationLocations[0])
            .destinationLocations{ this.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
            .selectDepartureLocation(eastDepartureLocations[0])
            .destinationLocations{ this.shouldContain(eastDestinationLocations[0].toDisplayedValue()) }
    }

    @Test
    fun `should display departure location back when different destination location is selected`() {
        page.get()
            .selectServer(archeageServer)
            .selectDestinationLocation(eastDestinationLocations[0])
            .departureLocations{ this.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
            .selectDestinationLocation(eastDestinationLocations[1])
            .departureLocations{ this.shouldContain(eastDestinationLocations[0].toDisplayedValue()) }
    }

    private fun getExpectedDepartureLocations(continent: Continent) =
        when(continent){
            Continent.EAST -> eastDepartureLocations.plus(eastDestinationLocations)
            Continent.WEST -> westDepartureLocations.plus(westDestinationLocations)
            Continent.NORTH -> TODO()
        }.map(Location::name).map(String::lowercase).map { it.capitalized() }.plus(messageSource.getMessage("page.locations.all", emptyArray(), LocaleContextHolder.getLocale()))

    private fun getExpectedDestinationLocations(continent: Continent) =
        when(continent){
            Continent.EAST -> eastDestinationLocations
            Continent.WEST -> westDestinationLocations
            Continent.NORTH -> TODO()
        }.map{ location -> location.toDisplayedValue() }.plus(messageSource.getMessage("page.factories.all", emptyArray(), LocaleContextHolder.getLocale()))

    private fun Location.toDisplayedValue() : String = name.lowercase().capitalized()


}