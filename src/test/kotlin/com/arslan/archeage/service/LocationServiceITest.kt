package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.org.apache.commons.lang3.arch.Processor.Arch

class LocationServiceITest(
    private val locationService: LocationService,
) : AbstractITest() {

    private lateinit var archeageServer: ArcheageServer
    private lateinit var anotherArcheageServer: ArcheageServer

    @BeforeEach
    fun prepareTestContext(){
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME_1"))
        anotherArcheageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME_2"))
    }

    @Test
    fun `should return empty list for continent of specific archeage server`() {
        locationService.continentLocations(Continent.WEST,archeageServer).shouldBeEmpty()
        locationService.continentFactories(Continent.WEST,archeageServer).shouldBeEmpty()

        locationRepository.save(Location("ANY_NAME",Continent.WEST,archeageServer,true))
        locationRepository.save(Location("ANY_NAME_2",Continent.NORTH,anotherArcheageServer,true))

        locationService.continentLocations(Continent.NORTH,archeageServer).shouldBeEmpty()
        locationService.continentFactories(Continent.NORTH,archeageServer).shouldBeEmpty()
    }

    @Test
    fun `should return continent locations of specific archeage server`() {
        val westLocation = locationRepository.save(Location("ANY_NAME_1",Continent.WEST,archeageServer))
        val northLocation = locationRepository.save(Location("ANY_NAME_2",Continent.NORTH,anotherArcheageServer))

        locationService.continentLocations(Continent.WEST,archeageServer).shouldContainExactly(westLocation)
        locationService.continentLocations(Continent.NORTH,archeageServer).shouldBeEmpty()
        locationService.continentLocations(Continent.NORTH,anotherArcheageServer).shouldContainExactly(northLocation)
    }

    @Test
    fun `should return continent factory location of specific archeage server`() {
        val westFactoryLocation = locationRepository.save(Location("WEST_FACTORY",Continent.WEST,archeageServer,true))
        val northLocation = locationRepository.save(Location("NORTH_FACTORY",Continent.NORTH,anotherArcheageServer,true))
        locationRepository.save(Location("WEST_NOT_FACTORY",Continent.WEST,archeageServer,false))
        locationRepository.save(Location("NORTH_NOT_FACTORY",Continent.NORTH,anotherArcheageServer,false))

        locationService.continentFactories(Continent.WEST,archeageServer).shouldContainExactly(westFactoryLocation)
        locationService.continentFactories(Continent.NORTH,archeageServer).shouldBeEmpty()
        locationService.continentFactories(Continent.NORTH,anotherArcheageServer).shouldContainExactly(northLocation)
        locationService.continentFactories(Continent.WEST,anotherArcheageServer).shouldBeEmpty()
    }
}