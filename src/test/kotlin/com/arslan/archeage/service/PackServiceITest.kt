package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackRequest
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.entity.pack.PackProfit
import com.arslan.archeage.entity.pack.PackProfitKey
import com.arslan.archeage.toDTO
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

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

    private val percentages = mutableMapOf<Long,Int>()

    private lateinit var category: Category

    private lateinit var anotherServerCategory: Category


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

        materialPrices[purchasableItem.id!!] = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableItem),randomPrice()))
        materialPrices[purchasableOfAnotherServer.id!!] = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableOfAnotherServer),randomPrice()))

        category = categoryRepository.save(Category("ANY_CATEGORY",null,archeageServer))
        anotherServerCategory = categoryRepository.save(Category("ANY_CATEGORY",null,anotherArcheageServer))
    }

    @AfterEach
    fun clear(){
        ArcheageServerContextHolder.clear()
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return empty list when trying to retrieve packs of continent which does not have any packs`(continent: Continent) {
        if(continent == westLocation.continent) return

        val pack = packRepository.save(Pack(westLocation, PackPrice(randomPrice(),secondWestLocation),1,category,"ANY_NAME","ANY_DESC"))

        packService.packs(PackRequest(continent,null,null,null),pageable,archeageServer).shouldBeEmpty()
        packService.packs(PackRequest(continent,pack.price.sellLocation.id,pack.creationLocation.id,null),pageable,archeageServer).shouldBeEmpty()
        packService.packs(PackRequest(continent,null,pack.creationLocation.id,null),pageable,archeageServer).shouldBeEmpty()
        packService.packs(PackRequest(continent,pack.price.sellLocation.id,null,null),pageable,archeageServer).shouldBeEmpty()
    }


    @Test
    fun `should only return packs of requested continent`() {
        preparePacksThatShouldNotBeIncludedInTheResult()

        val expectedWestPacks = listOf(createPack(westLocation,"ANY_NAME_1",secondWestLocation), createPack(westLocation,"ANY_NAME_2",secondWestLocation))
        val expectedEastPacks = listOf(createPack(eastLocation,"ANY_NAME_3",secondEastLocation), createPack(eastLocation,"ANY_NAME_4",secondEastLocation))
        val expectedNorthPacks = listOf(createPack(northLocation,"ANY_NAME_5",secondNorthLocation), createPack(northLocation,"ANY_NAME_6",secondNorthLocation))

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

        val expectedWestPacks = listOf(createPack(westLocation,"ANY_NAME_1",secondWestLocation), createPack(westLocation,"ANY_NAME_2",secondWestLocation))
        val expectedEastPacks = listOf(createPack(eastLocation,"ANY_NAME_3",secondEastLocation), createPack(eastLocation,"ANY_NAME_4",secondEastLocation))
        val expectedNorthPacks = listOf(createPack(northLocation,"ANY_NAME_5",secondNorthLocation), createPack(northLocation,"ANY_NAME_6",secondNorthLocation))

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

        val expectedWestPacks = listOf(createPack(westLocation,"ANY_NAME_1",secondWestLocation), createPack(westLocation,"ANY_NAME_2",secondWestLocation))
        val expectedEastPacks = listOf(createPack(eastLocation,"ANY_NAME_3",secondEastLocation), createPack(eastLocation,"ANY_NAME_4",secondEastLocation))
        val expectedNorthPacks = listOf(createPack(northLocation,"ANY_NAME_5",secondNorthLocation), createPack(northLocation,"ANY_NAME_6",secondNorthLocation))

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

        val expectedWestPacks = listOf(createPack(westLocation,"ANY_NAME_1",secondWestLocation), createPack(westLocation,"ANY_NAME_2",secondWestLocation))
        val expectedEastPacks = listOf(createPack(eastLocation,"ANY_NAME_3",secondEastLocation), createPack(eastLocation,"ANY_NAME_4",secondEastLocation))
        val expectedNorthPacks = listOf(createPack(northLocation,"ANY_NAME_5",secondNorthLocation), createPack(northLocation,"ANY_NAME_6",secondNorthLocation))

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

    @Test
    fun `should only return packs of requested continent sorted`() {
        val expectedPacksOrdered = listOf(createPack(westLocation,"ANY_NAME_2",secondWestLocation), createPack(westLocation,"ANY_NAME_1",secondWestLocation))
            .toDTO(materialPrices,percentages)
            .sortedByDescending { it.name }

        packService
            .packs(PackRequest(Continent.WEST), PageRequest.of(0,10,Sort.by(Sort.Order(Sort.Direction.DESC,"id.pack.name"))),archeageServer)
            .shouldContainInOrder(expectedPacksOrdered)
    }

    private fun preparePacksWithRandomDestinationLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1", Continent.WEST, archeageServer, true))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", Continent.EAST, archeageServer, true))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2", Continent.NORTH, archeageServer, true))

        createPack(westLocation,"RANDOM_DESTINATION_LOCATION_1",randomWestLocation)
        createPack(eastLocation,"RANDOM_DESTINATION_LOCATION_2",randomEastLocation)
        createPack(northLocation,"RANDOM_DESTINATION_LOCATION_3",randomNorthLocation)

        packRepository.save(Pack(westLocation, PackPrice(Price(1,1,1),randomWestLocation),1,category,"ANY_NAME_232","ANY_DESC"))
        packRepository.save(Pack(eastLocation,PackPrice(Price(1,1,1),randomEastLocation),1, category,"ANY_NAME_5234","ANY_DESC"))
        packRepository.save(Pack(northLocation, PackPrice(Price(1,1,1),randomNorthLocation),1,category,"ANY_NAME_123512","ANY_DESC"))
    }

    private fun preparePacksWithRandomDepartureLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1",Continent.WEST,archeageServer))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.EAST,archeageServer))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.NORTH,archeageServer))

        createPack(randomWestLocation,"RANDOM_DEPARTURE_LOCATION_1",secondWestLocation)
        createPack(randomEastLocation,"RANDOM_DEPARTURE_LOCATION_2",secondEastLocation)
        createPack(randomNorthLocation,"RANDOM_DEPARTURE_LOCATION_3",secondNorthLocation)
        testEntityManager.flush()
    }

    /**
     * These packs are excluded by default because they belong to different server.
     */
    private fun preparePacksThatShouldNotBeIncludedInTheResult(){
        createPack(westLocationOfDifferentServer,"NOT_INCLUDED_1",westLocationOfDifferentServer,purchasableOfAnotherServer,anotherServerCategory)
        createPack(westLocationOfDifferentServer,"NOT_INCLUDED_2",eastLocationOfDifferentServer,purchasableOfAnotherServer,anotherServerCategory)
        createPack(westLocationOfDifferentServer,"NOT_INCLUDED_3",northLocationOfDifferentServer,purchasableOfAnotherServer,anotherServerCategory)

        testEntityManager.flush()
    }

    private fun createPack(creationLocation: Location,name: String,sellLocation: Location,material: PurchasableItem = purchasableItem,packCategory: Category = category) : Pack{
        var pack = Pack(creationLocation,PackPrice(randomPrice(),sellLocation),nextInt(1,10),packCategory,name,"ANY_DESC").apply {
            addMaterial(CraftingMaterial(nextInt(1),material))
        }
        pack = packRepository.save(pack)
        packProfitRepository.save(PackProfit(PackProfitKey(pack,user), randomPrice()))
        percentages[pack.id!!] = 100

        return pack
    }
}
