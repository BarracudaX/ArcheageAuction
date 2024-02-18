package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.Continent.*
import com.arslan.archeage.entity.*
import io.kotest.matchers.collections.*
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource

class   PackServiceITest(private val packService: PackService) : AbstractITest() {

    private lateinit var archeageServer: ArcheageServer

    @BeforeEach
    fun prepareTestContext(){
        archeageServer = archeageServerRepository.save(ArcheageServer("SOME_ARCHEAGE_SERVER"))
    }

    @AfterEach
    fun clear(){
        ArcheageServerContextHolder.clear()
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return empty list when trying to retrieve packs of continent which does not have any packs`(continent: Continent) {
        createPack(archeageServer,continent)

        Continent
            .values()
            .filter { otherContinent -> otherContinent != continent }
            .forEach { otherContinent ->
                continentPacks(packService,otherContinent,archeageServer).shouldBeEmpty()
            }
    }


    @EnumSource(Continent::class)
    @ParameterizedTest
    fun `should only return packs of requested continent`(continent: Continent) {
        continentPacks(packService,continent,archeageServer).shouldBeEmpty()

        val expectedPack = createPack(archeageServer,continent)

        continentPacks(packService,continent,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedPack)
    }

    @Test
    fun `should only return packs of requested continent that have the provided departure location`() {
        createPacksWithDepartureLocations()
        packRepository.count().shouldNotBe(0)
        val departureLocation = locationRepository.save(Location("DEPARTURE_LOCATION",WEST,archeageServer,true))
        packsWithDepartureLocation(packService, WEST,archeageServer,departureLocation).shouldBeEmpty()

        val expectedPack = createPackWithDepartureLocation(archeageServer,departureLocation)

        packsWithDepartureLocation(packService,WEST,archeageServer,departureLocation).shouldNotBeEmpty().shouldContainExactly(expectedPack)
    }


    @Test
    fun `should only return packs of requested continent that have the provided destination location`() {
        createPacksWithDestinationLocation()
        val destinationLocation = locationRepository.save(Location("SOME_DESTINATION_LOCATION",WEST,archeageServer,true))
        packsWithDestinationLocation(packService,WEST,archeageServer,destinationLocation).shouldBeEmpty()

        val expectedPack = createPackWithDestinationLocation(archeageServer,destinationLocation)

        packsWithDestinationLocation(packService,WEST,archeageServer,destinationLocation).shouldNotBeEmpty().shouldContainExactly(expectedPack)
    }

    @Test
    fun `should return packs created at specified location and sold at specified location`() {
        createPacksWithDepartureLocations()
        createPacksWithDestinationLocation()
        val destinationLocation = locationRepository.save(Location("SOME_DESTINATION_LOCATION",WEST,archeageServer,true))
        val departureLocation = locationRepository.save(Location("SOME_DEPARTURE_LOCATION",WEST,archeageServer,true))
        packs(departureLocation,packService,WEST,archeageServer,destinationLocation).shouldBeEmpty()

        val expectedPack = createPack(destinationLocation,archeageServer,departureLocation,WEST)

        packs(departureLocation,packService,WEST,archeageServer,destinationLocation).shouldNotBeEmpty().shouldContainExactly(expectedPack)
    }

    @Test
    fun `should return packs that belong to any specified category`() {
        preparePacksWithRandomCategory()
        val category1 = categoryRepository.save(Category("ANY_CATEGORY_1",null,archeageServer))
        val category2 = categoryRepository.save(Category("ANY_CATEGORY_2",null,archeageServer))
        packsThatBelongToCategories(packService,WEST,archeageServer, listOf(category1,category2)).shouldBeEmpty()
        val expectedPacks = listOf(createPackWithCategory(archeageServer,category1,WEST), createPackWithCategory(archeageServer,category2,WEST))

        packsThatBelongToCategories(packService,WEST,archeageServer,listOf(category1,category2)).shouldContainExactlyInAnyOrder(expectedPacks)
        packsThatBelongToCategories(packService,WEST,archeageServer,listOf(category1)).shouldContainExactlyInAnyOrder(expectedPacks[0])
        packsThatBelongToCategories(packService,WEST,archeageServer,listOf(category2)).shouldContainExactlyInAnyOrder(expectedPacks[1])
    }

    private fun createPacksWithDestinationLocation(){
        val someWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1", WEST, archeageServer, true))
        val someEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", EAST, archeageServer, true))
        val someNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", NORTH, archeageServer, true))

        createPackWithDestinationLocation(archeageServer,someWestLocation)
        createPackWithDepartureLocation(archeageServer,someEastLocation)
        createPackWithDepartureLocation(archeageServer,someNorthLocation)

        testEntityManager.flush()
    }

    private fun createPacksWithDepartureLocations(){
        val someWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1", WEST,archeageServer))
        val someEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", EAST,archeageServer))
        val someNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", NORTH,archeageServer))

        createPackWithDepartureLocation(archeageServer,someWestLocation)
        createPackWithDepartureLocation(archeageServer,someEastLocation)
        createPackWithDepartureLocation(archeageServer,someNorthLocation)
        testEntityManager.flush()
    }

    private fun preparePacksWithRandomCategory(){
        val someCategory = categoryRepository.save(Category("SOME_CATEGORY",null,archeageServer))

        createPackWithCategory(archeageServer,someCategory,WEST)
        createPackWithCategory(archeageServer,someCategory,EAST)
        createPackWithCategory(archeageServer,someCategory,NORTH)
    }

}
