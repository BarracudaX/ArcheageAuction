package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.*
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class PackServiceITest(
    private val packService: PackService,
) : AbstractITest() {

    private lateinit var westLocation: Location

    private lateinit var eastLocation: Location

    private lateinit var northLocation: Location

    private lateinit var archeageServer: ArcheageServer

    @BeforeEach
    fun prepareTestContext(){
        westLocation = locationRepository.save(Location("ANY_NAME_1",Continent.WEST, Region.EUROPE))
        eastLocation = locationRepository.save(Location("ANY_NAME_2",Continent.EAST,Region.EUROPE))
        northLocation = locationRepository.save(Location("ANY_NAME_3",Continent.NORTH,Region.EUROPE))
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME",Region.EUROPE))
        ArcheageServerContextHolder.setServerContext(archeageServer)
    }

    @AfterEach
    fun clear(){
        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should return empty list when trying to retrieve packs of continent with no empty archeage server context holder`() {
        packRepository.save(Pack(westLocation,"ANY_NAME","ANY_DESCR"))
        ArcheageServerContextHolder.clear()

        packService.packs(westLocation.continent).shouldBeEmpty()
    }


    @MethodSource("continents")
    @ParameterizedTest
    fun `should return empty list when trying to retrieve packs of continent which does not have any packs`(continent: Continent) {
        if(continent == westLocation.continent) return

        packRepository.save(Pack(westLocation,"ANY_NAME","ANY_DESC"))

        packService.packs(continent).shouldBeEmpty()
    }


    @Test
    fun `should only return packs of requested continent that have at least one price at the currently specified server`() {
        val differentServer = archeageServerRepository.save(ArcheageServer("DIFFERENT_SERVER",Region.EUROPE))
        val expectedWestPacks = packRepository.saveAll(listOf(Pack(westLocation,"ANY_NAME_1","ANY_DESC_1"),Pack(westLocation,"ANY_NAME_2","ANY_DESC_2")))
        val expectedEastPacks = packRepository.saveAll(listOf(Pack(eastLocation,"ANY_NAME_3","ANY_DESC_3")))
        val expectedNorthPacks = packRepository.saveAll(listOf(Pack(northLocation,"ANY_NAME_4","ANY_DESC_4")))
        val westPackWithPriceOnDiffServer = packRepository.save(Pack(westLocation,"ANY_NAME_5","ANY_DESC_5")) //west pack with price on different server
        val eastPackWithPriceOnDiffServer = packRepository.save(Pack(eastLocation,"ANY_NAME_6","ANY_DESC_65")) //east pack with price on different server
        val northPackWithPriceOnDiffServer = packRepository.save(Pack(northLocation,"ANY_NAME_7","ANY_DESC_7")) //north pack with price on different server
        packRepository.save(Pack(westLocation,"ANY_NAME_5","ANY_DESC_5")) //west pack without price should not be included
        packRepository.save(Pack(eastLocation,"ANY_NAME_6","ANY_DESC_65")) //east pack without price should not be included
        packRepository.save(Pack(northLocation,"ANY_NAME_7","ANY_DESC_7")) //north pack without price should not be included
        itemPriceRepository.save(PackPrice(westPackWithPriceOnDiffServer,differentServer,Price(10,10,10),westPackWithPriceOnDiffServer.creationLocation))
        itemPriceRepository.save(PackPrice(eastPackWithPriceOnDiffServer,differentServer,Price(10,10,10),eastPackWithPriceOnDiffServer.creationLocation))
        itemPriceRepository.save(PackPrice(northPackWithPriceOnDiffServer,differentServer,Price(10,10,10),northPackWithPriceOnDiffServer.creationLocation))

        for(pack in expectedEastPacks.plus(expectedWestPacks).plus(expectedNorthPacks)){
            itemPriceRepository.save(PackPrice(pack,archeageServer, Price(10,10,10),pack.creationLocation))
        }


        packService.packs(Continent.WEST).shouldContainExactlyInAnyOrder(expectedWestPacks)
        packService.packs(Continent.EAST).shouldContainExactlyInAnyOrder(expectedEastPacks)
        packService.packs(Continent.NORTH).shouldContainExactlyInAnyOrder(expectedNorthPacks)
    }


}