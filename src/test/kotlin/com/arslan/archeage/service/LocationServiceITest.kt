package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Region
import com.arslan.archeage.repository.LocationRepository
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LocationServiceITest(
    private val locationService: LocationService,
) : AbstractITest() {

    @Test
    fun `should return empty result when requested locations for continent which does not have any locations`() {
        assertTrue(locationService.continentLocations(Continent.EAST).isEmpty())

        locationRepository.save(Location("ANY",Continent.EAST, Region.CIS))

        locationService.continentLocations(Continent.WEST).shouldBeEmpty()
    }

    @Test
    fun `should return locations of the requested continent`() {
        val expectedEastContinent = locationRepository.saveAll(listOf(Location("ANY_1",Continent.EAST,Region.CIS),Location("ANY_2",Continent.EAST,Region.EUROPE)))
        val expectedWestContinent = locationRepository.saveAll(listOf(Location("ANY_3",Continent.WEST,Region.CIS)))
        val expectedNorthContinent = locationRepository.saveAll(listOf(Location("ANY_5",Continent.NORTH,Region.CIS)))

        locationService.continentLocations(Continent.EAST).shouldContainExactlyInAnyOrder(expectedEastContinent)
        locationService.continentLocations(Continent.WEST).shouldContainExactlyInAnyOrder(expectedWestContinent)
        locationService.continentLocations(Continent.NORTH).shouldContainExactlyInAnyOrder(expectedNorthContinent)
    }

    @Test
    fun `should return empty list when requested factories of continent that does not have any`() {
        locationService.continentFactories(Continent.EAST).shouldBeEmpty()

        locationRepository.save(Location("NOT_A_FACTORY_LOCATION",Continent.EAST,Region.CIS,false))

        locationService.continentFactories(Continent.EAST).shouldBeEmpty()
    }

    @Test
    fun `should return factories of requested continent`() {
        val expectedEastFactories = locationRepository.saveAll(listOf(Location("EAST_FACTOR_1",Continent.EAST,Region.CIS,true),Location("EAST_FACTOR_2",Continent.EAST,Region.EUROPE,true)))
        val expectedWestFactories = locationRepository.saveAll(listOf(Location("WEST_FACTOR_1",Continent.WEST,Region.EUROPE,true),Location("EAST_FACTOR_2",Continent.WEST,Region.CIS,true)))
        val expectedNorthFactories = locationRepository.saveAll(listOf(Location("WEST_FACTOR_1",Continent.NORTH,Region.CIS,true),Location("EAST_FACTOR_2",Continent.NORTH,Region.EUROPE,true)))
        locationRepository.save(Location("EAST_NOT_FACTORY",Continent.EAST,Region.CIS,false))
        locationRepository.save(Location("WEST_NOT_FACTORY",Continent.WEST,Region.EUROPE,false))
        locationRepository.save(Location("NORTH_NOT_FACTORY",Continent.NORTH,Region.EUROPE,false))

        locationService.continentFactories(Continent.EAST).shouldContainExactlyInAnyOrder(expectedEastFactories)
        locationService.continentFactories(Continent.WEST).shouldContainExactlyInAnyOrder(expectedWestFactories)
        locationService.continentFactories(Continent.NORTH).shouldContainExactlyInAnyOrder(expectedNorthFactories)
    }


}