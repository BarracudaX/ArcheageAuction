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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class PackagesPageLocationTest : SeleniumTest(){

    private lateinit var page: PackagesPageObject
    private lateinit var archeageServer: ArcheageServer
    private val eastDepartureLocations = mutableListOf<Location>()
    private val eastDestinationLocations = mutableListOf<Location>()
    private val westDepartureLocations = mutableListOf<Location>()
    private val westDestinationLocations = mutableListOf<Location>()

    @BeforeEach
    override fun setUp() {
        super.setUp()
        archeageServer = createArcheageServer("SOME_ARCHEAGE_SERVER")
        eastDepartureLocations.add(createEastDepartureLocation("SOME_EAST_DEPARTURE_LOCATION_1",archeageServer))
        eastDepartureLocations.add(createEastDepartureLocation("SOME_EAST_DEPARTURE_LOCATION_2",archeageServer))
        westDepartureLocations.add(createWestDepartureLocation("SOME_WEST_DEPARTURE_LOCATION_1",archeageServer))
        westDepartureLocations.add(createWestDepartureLocation("SOME_WEST_DEPARTURE_LOCATION_2",archeageServer))
        westDestinationLocations.add(createWestDestinationLocation("SOME_WEST_DESTINATION_LOCATION_1",archeageServer))
        westDestinationLocations.add(createWestDestinationLocation("SOME_WEST_DESTINATION_LOCATION_2",archeageServer))
        eastDestinationLocations.add(createEastDestinationLocation("SOME_EAST_DESTINATION_LOCATION_1",archeageServer))
        eastDestinationLocations.add(createEastDestinationLocation("SOME_EAST_DESTINATION_LOCATION_2",archeageServer))
        page = PackagesPageObject(webDriver,port).get()
    }

    @Test
    fun `should display departure locations of selected continent`() {
        page
            .selectServer(archeageServer)
            .departureLocations{ departureLocations -> departureLocations.shouldContainExactlyInAnyOrder(getExpectedDepartureLocations(Continent.EAST)) }
            .selectContinent(Continent.WEST,westDepartureLocations[0])
            .departureLocations{ departureLocations -> departureLocations.shouldContainExactlyInAnyOrder(getExpectedDepartureLocations(Continent.WEST)) }
    }

    @Test
    fun `should display destination locations of selected continent`() {
        page
            .selectServer(archeageServer)
            .destinationLocations{ destinationLocations -> destinationLocations.shouldContainExactlyInAnyOrder(getExpectedDestinationLocations(Continent.EAST)) }
            .selectContinent(Continent.WEST,westDestinationLocations[0])
            .destinationLocations{ destinationLocations -> destinationLocations.shouldContainExactlyInAnyOrder(getExpectedDestinationLocations(Continent.WEST)) }
    }

    @Test
    fun `should display empty destination and departure locations when archeage server is not selected`() {
        page
            .destinationLocations{ strings -> strings.shouldHaveSize(0) }
            .departureLocations{ strings -> strings.shouldHaveSize(0) }
    }

    @Test
    fun `should not display destination location if it is selected as departure location`() {
        page
            .selectServer(archeageServer)
            .destinationLocations{ destinationLocations -> destinationLocations.shouldContain(eastDestinationLocations[0].toDisplayedValue())  }
            .selectDepartureLocation(eastDestinationLocations[0])
            .destinationLocations{ destinationLocations -> destinationLocations.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
    }

    @Test
    fun `should not display departure location if it is selected as destination location`() {
        page
            .selectServer(archeageServer)
            .departureLocations{ departureLocations -> departureLocations.shouldContain(eastDestinationLocations[0].toDisplayedValue()) }
            .selectDestinationLocation(eastDestinationLocations[0])
            .departureLocations{ departureLocations -> departureLocations.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
    }

    @Test
    fun `should display destination location back when different departure location is selected`() {
        page.get()
            .selectServer(archeageServer)
            .selectDepartureLocation(eastDestinationLocations[0])
            .destinationLocations{ destinationLocations -> destinationLocations.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
            .selectDepartureLocation(eastDepartureLocations[0])
            .destinationLocations{ destinationLocations -> destinationLocations.shouldContain(eastDestinationLocations[0].toDisplayedValue()) }
    }

    @Test
    fun `should display departure location back when different destination location is selected`() {
        page.get()
            .selectServer(archeageServer)
            .selectDestinationLocation(eastDestinationLocations[0])
            .departureLocations{ departureLocation -> departureLocation.shouldNotContain(eastDestinationLocations[0].toDisplayedValue()) }
            .selectDestinationLocation(eastDestinationLocations[1])
            .departureLocations{ departureLocation -> departureLocation.shouldContain(eastDestinationLocations[0].toDisplayedValue()) }
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