package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Region
import com.arslan.archeage.repository.LocationRepository
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocationServiceITest(
    private val locationService: LocationService,
) : AbstractITest() {

    private lateinit var archeageServer: ArcheageServer

    @BeforeEach
    fun prepareTestContext(){
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME_1",Region.CIS))
        ArcheageServerContextHolder.setServerContext(archeageServer)
    }

    @AfterEach
    fun clear(){
        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should return empty result when requested locations for continent which does not have any locations`() {
        assertTrue(locationService.continentLocations(Continent.EAST).isEmpty())

        locationRepository.save(Location("ANY",Continent.EAST, archeageServer.region))

        locationService.continentLocations(Continent.WEST).shouldBeEmpty()
    }

    @Test
    fun `should return empty result when requested locations and archeage server context holder is empty`() {
        ArcheageServerContextHolder.clear()

        locationRepository.save(Location("ANY",Continent.EAST,archeageServer.region))

        locationService.continentLocations(Continent.EAST).shouldBeEmpty()
    }

    @Test
    fun `should return locations of the requested continent but only if they belong to the user server region`() {
        val expectedEastContinent = locationRepository.saveAll(listOf(Location("ANY_1",Continent.EAST,archeageServer.region),Location("ANY_2",Continent.EAST,archeageServer.region)))
        val expectedWestContinent = locationRepository.saveAll(listOf(Location("ANY_3",Continent.WEST,archeageServer.region)))
        val expectedNorthContinent = locationRepository.saveAll(listOf(Location("ANY_4",Continent.NORTH,archeageServer.region)))
        locationRepository.save(Location("ANY_5",Continent.EAST,Region.EUROPE)) // east location belonging to different region should not be included in the result
        locationRepository.save(Location("ANY_6",Continent.WEST,Region.EUROPE)) // west location belonging to different region should not be included in the result
        locationRepository.save(Location("ANY_7",Continent.NORTH,Region.EUROPE)) // west location belonging to different region should not be included in the result


        locationService.continentLocations(Continent.EAST).shouldContainExactlyInAnyOrder(expectedEastContinent)
        locationService.continentLocations(Continent.WEST).shouldContainExactlyInAnyOrder(expectedWestContinent)
        locationService.continentLocations(Continent.NORTH).shouldContainExactlyInAnyOrder(expectedNorthContinent)
    }

    @Test
    fun `should return empty list when requested factories of continent that does not have any`() {
        locationService.continentFactories(Continent.EAST).shouldBeEmpty()

        locationRepository.save(Location("NOT_A_FACTORY_LOCATION",Continent.EAST,archeageServer.region,false))

        locationService.continentFactories(Continent.EAST).shouldBeEmpty()
    }

    @Test
    fun `should return empty list when requested factories and archeage server context holder is empty`() {
        locationRepository.save(Location("FACTORY",Continent.EAST,archeageServer.region,true))
        ArcheageServerContextHolder.clear()

        locationService.continentFactories(Continent.EAST).shouldBeEmpty()
    }

    @Test
    fun `should return factories of requested continent that belong to the currently specified archeage server region`() {
        val expectedEastFactories = locationRepository.saveAll(listOf(Location("EAST_FACTOR_1",Continent.EAST,archeageServer.region,true),Location("EAST_FACTOR_2",Continent.EAST,archeageServer.region,true)))
        val expectedWestFactories = locationRepository.saveAll(listOf(Location("WEST_FACTOR_1",Continent.WEST,archeageServer.region,true),Location("EAST_FACTOR_2",Continent.WEST,archeageServer.region,true)))
        val expectedNorthFactories = locationRepository.saveAll(listOf(Location("WEST_FACTOR_1",Continent.NORTH,archeageServer.region,true),Location("EAST_FACTOR_2",Continent.NORTH,archeageServer.region,true)))
        locationRepository.save(Location("EAST_NOT_FACTORY",Continent.EAST,archeageServer.region,false)) // east location that is not a factory should not be included in the result
        locationRepository.save(Location("WEST_NOT_FACTORY",Continent.WEST,archeageServer.region,false)) // west location that is not a factory should not be included in the result
        locationRepository.save(Location("NORTH_NOT_FACTORY",Continent.NORTH,archeageServer.region,false)) //north location that is not a factory
        locationRepository.save(Location("EAST_FACTORY_DIFFERENT_REGION",Continent.EAST,Region.EUROPE,true)) // east location factory that belongs to different region should not be included in the result
        locationRepository.save(Location("WEST_FACTORY_DIFFERENT_REGION",Continent.WEST,Region.EUROPE,true)) // west location factory that belongs to different region should not be included in the result
        locationRepository.save(Location("NORTH_FACTORY_DIFFERENT_REGION",Continent.NORTH,Region.EUROPE,true)) // north location factory that belongs to different region should not be included in the result


        locationService.continentFactories(Continent.EAST).shouldContainExactlyInAnyOrder(expectedEastFactories)
        locationService.continentFactories(Continent.WEST).shouldContainExactlyInAnyOrder(expectedWestFactories)
        locationService.continentFactories(Continent.NORTH).shouldContainExactlyInAnyOrder(expectedNorthFactories)
    }


}