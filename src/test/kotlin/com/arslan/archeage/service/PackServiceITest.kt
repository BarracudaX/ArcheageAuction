package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackRequest
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.entity.pack.PackProfit
import com.arslan.archeage.entity.pack.PackProfitKey
import com.arslan.archeage.toDTO
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.domain.Pageable

class   PackServiceITest(
    private val packService: PackService
) : AbstractITest() {

    private lateinit var westLocation: Location

    private lateinit var secondWestLocation: Location

    private lateinit var thirdWestLocation: Location

    private lateinit var westLocationOfDifferentServer:Location

    private lateinit var eastLocationOfDifferentServer: Location

    private lateinit var northLocationOfDifferentServer: Location

    private lateinit var eastLocation: Location

    private lateinit var secondEastLocation: Location

    private lateinit var thirdEastLocation: Location

    private lateinit var northLocation: Location

    private lateinit var secondNorthLocation: Location

    private lateinit var thirdNorthLocation: Location

    private lateinit var archeageServer: ArcheageServer

    private lateinit var anotherArcheageServer: ArcheageServer

    private lateinit var user: User

    private lateinit var purchasableItem: PurchasableItem

    private lateinit var purchasableOfAnotherServer: PurchasableItem

    private val materialPrices = mutableMapOf<Long,UserPrice>()

    private val pageable = Pageable.unpaged()

    private val percentages = mutableMapOf<Long,Double>()


    @BeforeEach
    fun prepareTestContext(){
        user = userRepository.save(User("ANY_EMAIL","ANY_PASSWORD"))

        archeageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME"))
        anotherArcheageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME_2"))

        westLocation = locationRepository.save(Location("ANY_NAME_1", Continent.WEST, archeageServer, true))
        eastLocation = locationRepository.save(Location("ANY_NAME_2", Continent.EAST, archeageServer, true))
        northLocation = locationRepository.save(Location("ANY_NAME_3", Continent.NORTH, archeageServer, true))
        secondWestLocation = locationRepository.save(Location("ANY_NAME_4", Continent.WEST, archeageServer, true))
        secondEastLocation = locationRepository.save(Location("ANY_NAME_123123", Continent.EAST, archeageServer, true))
        westLocationOfDifferentServer = locationRepository.save(Location("ANY_NAME_0923123",Continent.WEST,anotherArcheageServer,true))
        eastLocationOfDifferentServer = locationRepository.save(Location("ANY_NAME_0923124",Continent.EAST,anotherArcheageServer,true))
        northLocationOfDifferentServer = locationRepository.save(Location("ANY_NAME_0923124",Continent.NORTH,anotherArcheageServer,true))
        secondNorthLocation = locationRepository.save(Location("ANY_NAME_31231233", Continent.NORTH, archeageServer, true))
        thirdWestLocation = locationRepository.save(Location("THIRD_WEST_LOCATION", Continent.WEST, archeageServer, true))
        thirdNorthLocation = locationRepository.save(Location("THIRD_NORTH_LOCATION", Continent.NORTH, archeageServer, true))
        thirdEastLocation = locationRepository.save(Location("THIRD_EAST_LOCATION", Continent.EAST, archeageServer, true))

        purchasableItem = purchasableItemRepository.save(PurchasableItem("MATERIAL_1","MATERIAL_1",archeageServer))
        purchasableOfAnotherServer = purchasableItemRepository.save(PurchasableItem("MATERIAL_1","MATERIAL_1",anotherArcheageServer))
    }

    @AfterEach
    fun clear(){
        ArcheageServerContextHolder.clear()
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return empty list when trying to retrieve packs of continent which does not have any packs`(continent: Continent) {
        if(continent == westLocation.continent) return

        val pack = packRepository.save(Pack(westLocation, PackPrice(Price(1,1,1),secondWestLocation),1,"ANY_NAME","ANY_DESC"))

        packService.packs(PackRequest(continent,null,null,null),pageable,archeageServer).shouldBeEmpty()
        packService.packs(PackRequest(continent,pack.price.sellLocation.id,pack.creationLocation.id,null),pageable,archeageServer).shouldBeEmpty()
        packService.packs(PackRequest(continent,null,pack.creationLocation.id,null),pageable,archeageServer).shouldBeEmpty()
        packService.packs(PackRequest(continent,pack.price.sellLocation.id,null,null),pageable,archeageServer).shouldBeEmpty()
    }


    @Test
    fun `should only return packs of requested continent`() {
        preparePacksThatShouldNotBeIncludedInTheResult()

        val expectedWestPacks = packRepository.saveAll(listOf(
            Pack(westLocation, PackPrice(Price(1,1,1),secondWestLocation),1, "ANY_NAME_1","ANY_DESC_1").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(westLocation, PackPrice(Price(1,1,1),secondWestLocation),1,"ANY_NAME_2","ANY_DESC_2").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedEastPacks = packRepository.saveAll(listOf(
            Pack(eastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_NAME_3","ANY_DESC_3").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(eastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_NAME_4","ANY_DESC_4").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedNorthPacks = packRepository.saveAll(listOf(
            Pack(northLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_NAME_5","ANY_DESC_5").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(northLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_NAME_6","ANY_DESC_6").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        createUserPrice(expectedWestPacks.plus(expectedEastPacks).plus(expectedNorthPacks))

        packService.packs(PackRequest(Continent.WEST,null,null,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedWestPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.EAST,null,null,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedEastPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.NORTH,null,null,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedNorthPacks.toDTO(materialPrices,percentages))
    }

    @Test
    fun `should only return packs of requested continent that have the provided departure location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDepartureLocation()

        val expectedWestPacks = packRepository.saveAll(listOf(
            Pack(westLocation, PackPrice(Price(1,1,1),secondWestLocation),1,"ANY_PACK_NAME_2312","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(westLocation, PackPrice(Price(1,1,1),secondWestLocation),1,"ANY_PACK_NAME_1231232","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedEastPacks = packRepository.saveAll(listOf(
            Pack(eastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_PACK_NAME_4324","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(eastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_PACK_NAME_215123","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedNorthPacks = packRepository.saveAll(listOf(
            Pack(northLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_PACK_NAME_34093","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(northLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_PACK_NAME_09433","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        createUserPrice(expectedWestPacks.plus(expectedEastPacks).plus(expectedNorthPacks))

        packService.packs(PackRequest(Continent.WEST,westLocation.id,null,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedWestPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.EAST,eastLocation.id,null,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedEastPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.NORTH,northLocation.id,null,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedNorthPacks.toDTO(materialPrices,percentages))
    }

    /**
     * Destination location = sell location.
     */
    @Test
    fun `should only return packs of requested continent that have the provided destination location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDestinationLocation()

        val expectedWestPacks = packRepository.saveAll(listOf(
            Pack(westLocation, PackPrice(Price(1,1,1),secondWestLocation),1,"ANY_PACK_NAME_290923","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(thirdWestLocation,PackPrice(Price(1,1,1),secondWestLocation),1, "ANY_PACK_NAME_12311","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedEastPacks = packRepository.saveAll(listOf(
            Pack(eastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_PACK_NAME_34893","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(thirdEastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_PACK_NAME_231123","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedNorthPacks = packRepository.saveAll(listOf(
            Pack(northLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_PACK_NAME_090123","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(thirdNorthLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_PACK_NAME_909213","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        createUserPrice(expectedWestPacks.plus(expectedEastPacks).plus(expectedNorthPacks))

        packService.packs(PackRequest(Continent.WEST,null,secondWestLocation.id,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedWestPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.EAST,null,secondEastLocation.id,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedEastPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.NORTH,null,secondNorthLocation.id,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedNorthPacks.toDTO(materialPrices,percentages))
    }

    @Test
    fun `should return packs created at specified location and sold at specified location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDepartureLocation()
        preparePacksWithRandomDestinationLocation()

        val expectedWestPacks = packRepository.saveAll(listOf(
            Pack(westLocation, PackPrice(Price(1,1,1),secondWestLocation),1,"ANY_PACK_NAME_87244","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(westLocation,PackPrice(Price(1,1,1),secondWestLocation),1, "ANY_PACK_NAME_123781","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedEastPacks = packRepository.saveAll(listOf(
            Pack(eastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_PACK_NAME_39489143","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(eastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_PACK_NAME_13874873","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        val expectedNorthPacks = packRepository.saveAll(listOf(
            Pack(northLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_PACK_NAME_49812315","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) },
            Pack(northLocation,PackPrice(Price(1,1,1),secondNorthLocation),1, "ANY_PACK_NAME_437887823","ANY_DESC").apply { addMaterial(CraftingMaterial(1,purchasableItem)) }
        )).onEach { pack -> percentages[pack.id!!]=1.0 }
        createUserPrice(expectedWestPacks.plus(expectedEastPacks).plus(expectedNorthPacks))

        packService.packs(PackRequest(Continent.WEST,westLocation.id,secondWestLocation.id,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedWestPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.EAST,eastLocation.id,secondEastLocation.id,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedEastPacks.toDTO(materialPrices,percentages))
        packService.packs(PackRequest(Continent.NORTH,northLocation.id,secondNorthLocation.id,null),pageable,archeageServer).content
            .shouldNotBeEmpty()
            .shouldContainExactlyInAnyOrder(expectedNorthPacks.toDTO(materialPrices,percentages))
    }

    private fun preparePacksWithRandomDestinationLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1", Continent.WEST, archeageServer, true))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", Continent.EAST, archeageServer, true))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", Continent.NORTH, archeageServer, true))

        packRepository.save(Pack(westLocation, PackPrice(Price(1,1,1),randomWestLocation),1,"ANY_NAME_232","ANY_DESC"))
        packRepository.save(Pack(eastLocation,PackPrice(Price(1,1,1),randomEastLocation),1, "ANY_NAME_5234","ANY_DESC"))
        packRepository.save(Pack(northLocation, PackPrice(Price(1,1,1),randomNorthLocation),1,"ANY_NAME_123512","ANY_DESC"))
    }

    private fun preparePacksWithRandomDepartureLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1",Continent.WEST,archeageServer))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.EAST,archeageServer))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.NORTH,archeageServer))

        packRepository.save(Pack(randomWestLocation, PackPrice(Price(1,1,1),secondWestLocation),1,"ANY_NAME_4309043","ANY_DESC"))
        packRepository.save(Pack(randomEastLocation, PackPrice(Price(1,1,1),secondEastLocation),1,"ANY_NAME_313414","ANY_DESC"))
        packRepository.save(Pack(randomNorthLocation, PackPrice(Price(1,1,1),secondNorthLocation),1,"ANY_NAME_12354234","ANY_DESC"))
        testEntityManager.flush()
    }

    /**
     * These packs are excluded by default because they belong to different server.
     */
    private fun preparePacksThatShouldNotBeIncludedInTheResult(){
        val packs = mutableListOf<Pack>()
        packRepository.save(Pack(westLocationOfDifferentServer,PackPrice(Price(1,1,1),westLocationOfDifferentServer),1, "ANY_NAME_7","ANY_DESC_7"))
            .apply { addMaterial(CraftingMaterial(1,purchasableOfAnotherServer)) }.also { packs.add(it) }
        packRepository.save(Pack(eastLocationOfDifferentServer, PackPrice(Price(1,1,1),eastLocationOfDifferentServer),1,"ANY_NAME_8","ANY_DESC_8"))
            .apply { addMaterial(CraftingMaterial(1,purchasableOfAnotherServer)) }.also { packs.add(it) }
        packRepository.save(Pack(northLocationOfDifferentServer, PackPrice(Price(1,1,1),northLocationOfDifferentServer),1,"ANY_NAME_9","ANY_DESC_9"))
            .apply { addMaterial(CraftingMaterial(1,purchasableOfAnotherServer)) }.also { packs.add(it) }

        createUserPrice(packs)

        testEntityManager.flush()
    }

    private fun createUserPrice(packs: List<Pack>){
        packs.forEach { pack ->
            packProfitRepository.save(PackProfit(PackProfitKey(pack,user), Price(1,1,1)))
        }
    }


}