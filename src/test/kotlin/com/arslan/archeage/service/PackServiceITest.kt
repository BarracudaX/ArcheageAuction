package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.*
import com.arslan.archeage.toDTO
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.random.Random

/**
 * All pack service method that return packs, return only those packs that have at least one price and at least one recipe.Also, pack service returns packs based on
 * the current value of that is held in ArcheageServerContextHolder - specifically, it returns packs that belong to the server of the held value.
 * Finally, all methods returning pack return them sorted by profit.
 * This is a precondition for all tests that are testing these methods.
 */
class PackServiceITest(
    private val packService: PackService,
) : AbstractITest() {

    private lateinit var westLocation: Location

    private lateinit var secondWestLocation: Location

    private lateinit var eastLocation: Location

    private lateinit var secondEastLocation: Location

    private lateinit var northLocation: Location

    private lateinit var secondNorthLocation: Location

    private lateinit var archeageServer: ArcheageServer

    private val materials = mutableListOf<Item>()

    private val materialPrices = mutableMapOf<String,ItemPrice>()


    @BeforeEach
    fun prepareTestContext(){
        westLocation = locationRepository.save(Location("ANY_NAME_1",Continent.WEST, Region.EUROPE))
        eastLocation = locationRepository.save(Location("ANY_NAME_2",Continent.EAST,Region.EUROPE))
        northLocation = locationRepository.save(Location("ANY_NAME_3",Continent.NORTH,Region.EUROPE))
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME",Region.EUROPE))
        secondWestLocation = locationRepository.save(Location("ANY_NAME_4",Continent.WEST,Region.EUROPE,true))
        secondEastLocation = locationRepository.save(Location("ANY_NAME_123123",Continent.EAST,Region.EUROPE))
        secondNorthLocation = locationRepository.save(Location("ANY_NAME_31231233",Continent.NORTH,Region.EUROPE))
        ArcheageServerContextHolder.setServerContext(archeageServer)
        materials.add(itemRepository.save(Item("MATERIAL_1","MATERIAL_1",Region.EUROPE, mutableSetOf())).apply {
            val price = itemPriceRepository.save(ItemPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE))))
            prices.add(price)
            materialPrices[name] = price
        })
        materials.add(itemRepository.save(Item("MATERIAL_2","MATERIAL_2",Region.EUROPE, mutableSetOf())).apply {
            val price = itemPriceRepository.save(ItemPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE))))
            prices.add(price)
            materialPrices[name] = price
        })
        materials.add(itemRepository.save(Item("MATERIAL_3","MATERIAL_3",Region.EUROPE, mutableSetOf())).apply {
            val price = itemPriceRepository.save(ItemPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE))))
            prices.add(price)
            materialPrices[name] = price
        })
    }

    @AfterEach
    fun clear(){
        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should return empty list when trying to retrieve packs of continent with empty archeage server context holder`() {
        ArcheageServerContextHolder.clear()
        val pack = packRepository.save(Pack(westLocation,"ANY_NAME","ANY_DESCR"))
        val packPrice = itemPriceRepository.save(PackPrice(pack,archeageServer,Price(1,1,1),secondWestLocation))

        packService.packs(pack.creationLocation.continent).shouldBeEmpty()
        packService.packs(pack.creationLocation.continent,pack.creationLocation.name,packPrice.sellLocation.name).shouldBeEmpty()
        packService.packsAt(pack.creationLocation.continent,pack.creationLocation.name).shouldBeEmpty()
        packService.packsTo(pack.creationLocation.continent,packPrice.sellLocation.name).shouldBeEmpty()
    }


    @MethodSource("continents")
    @ParameterizedTest
    fun `should return empty list when trying to retrieve packs of continent which does not have any packs`(continent: Continent) {
        if(continent == westLocation.continent) return

        val pack = packRepository.save(Pack(westLocation,"ANY_NAME","ANY_DESC"))
        val packPrice = itemPriceRepository.save(PackPrice(pack,archeageServer,Price(1,1,1),secondWestLocation))

        packService.packs(continent).shouldBeEmpty()
        packService.packs(continent,pack.creationLocation.name,packPrice.sellLocation.name).shouldBeEmpty()
        packService.packsAt(continent,pack.creationLocation.name).shouldBeEmpty()
        packService.packsTo(continent,packPrice.sellLocation.name).shouldBeEmpty()
    }


    @Test
    fun `should only return packs of requested continent that have at least one price and one recipe at the currently specified server sorted by profit`() {
        preparePacksThatShouldNotBeIncludedInTheResult()

        val expectedWestPacks = packRepository.saveAll(listOf(Pack(westLocation,"ANY_NAME_1","ANY_DESC_1"),Pack(westLocation,"ANY_NAME_2","ANY_DESC_2")))
        val expectedEastPacks = packRepository.saveAll(listOf(Pack(eastLocation,"ANY_NAME_3","ANY_DESC_3"),Pack(eastLocation,"ANY_NAME_4","ANY_DESC_4")))
        val expectedNorthPacks = packRepository.saveAll(listOf(Pack(northLocation,"ANY_NAME_5","ANY_DESC_5"),Pack(northLocation,"ANY_NAME_6","ANY_DESC_6")))

       makePricesAndRecipesFor(expectedEastPacks.plus(expectedWestPacks).plus(expectedNorthPacks))

        packService.packs(Continent.WEST).shouldNotBeEmpty().shouldContainExactly(expectedWestPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.EAST).shouldNotBeEmpty().shouldContainExactly(expectedEastPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.NORTH).shouldNotBeEmpty().shouldContainExactly(expectedNorthPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    @Test
    fun `should only return packs of requested continent that have the provided departure location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePackWithDifferentDepartureLocation()

        val expectedWestLocations = packRepository.saveAll(listOf(Pack(westLocation,"ANY_PACK_NAME_2312","ANY_DESC"),Pack(westLocation,"ANY_PACK_NAME_1231232","ANY_DESC")))
        val expectedEastLocations = packRepository.saveAll(listOf(Pack(eastLocation,"ANY_PACK_NAME_4324","ANY_DESC"),Pack(eastLocation,"ANY_PACK_NAME_215123","ANY_DESC")))
        val expectedNorthLocations = packRepository.saveAll(listOf(Pack(northLocation,"ANY_PACK_NAME_34093","ANY_DESC"),Pack(northLocation,"ANY_PACK_NAME_09433","ANY_DESC")))

        makePricesAndRecipesFor(expectedEastLocations.plus(expectedWestLocations).plus(expectedNorthLocations))

        packService.packsAt(Continent.WEST,westLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedWestLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsAt(Continent.EAST,eastLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedEastLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsAt(Continent.NORTH,northLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedNorthLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    private fun makePricesAndRecipesFor(packs: List<Pack>){
        for(pack in packs){
            itemPriceRepository.save(PackPrice(pack, archeageServer, Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)), pack.creationLocation))
            recipeRepository.save(Recipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
        }
    }

    private fun preparePackWithDifferentDepartureLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1",Continent.WEST,Region.EUROPE))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.EAST,Region.EUROPE))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.NORTH,Region.EUROPE))

        packRepository.save(Pack(randomWestLocation,"ANY_NAME_232","ANY_DESC")).let { pack ->
            recipeRepository.save(Recipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            itemPriceRepository.save(PackPrice(pack,archeageServer,Price(1,1,1),secondWestLocation))
        }
        packRepository.save(Pack(randomEastLocation,"ANY_NAME_5234","ANY_DESC")).let { pack ->
            recipeRepository.save(Recipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            itemPriceRepository.save(PackPrice(pack,archeageServer,Price(1,1,1),secondEastLocation))
        }
        packRepository.save(Pack(randomNorthLocation,"ANY_NAME_123512","ANY_DESC")).let { pack ->
            recipeRepository.save(Recipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            itemPriceRepository.save(PackPrice(pack,archeageServer,Price(1,1,1),secondNorthLocation))
        }
        testEntityManager.flush()
    }

    private fun preparePacksThatShouldNotBeIncludedInTheResult(){
        //packs that have price for different server should not be included if user's chosen server is different.
        //packs that do not have any price, should not be returned.
        val differentServer = archeageServerRepository.save(ArcheageServer("DIFFERENT_SERVER",Region.EUROPE))
        val westPackWithPriceOnDiffServer = packRepository.save(Pack(westLocation,"ANY_NAME_7","ANY_DESC_7")) //west pack with price on different server
        val eastPackWithPriceOnDiffServer = packRepository.save(Pack(eastLocation,"ANY_NAME_8","ANY_DESC_8")) //east pack with price on different server
        val northPackWithPriceOnDiffServer = packRepository.save(Pack(northLocation,"ANY_NAME_9","ANY_DESC_9")) //north pack with price on different server
        recipeRepository.save(Recipe(westPackWithPriceOnDiffServer,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
        recipeRepository.save(Recipe(eastPackWithPriceOnDiffServer,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
        recipeRepository.save(Recipe(northPackWithPriceOnDiffServer,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
        itemPriceRepository.save(PackPrice(westPackWithPriceOnDiffServer,differentServer,Price(10,10,10),westPackWithPriceOnDiffServer.creationLocation))
        itemPriceRepository.save(PackPrice(eastPackWithPriceOnDiffServer,differentServer,Price(10,10,10),eastPackWithPriceOnDiffServer.creationLocation))
        itemPriceRepository.save(PackPrice(northPackWithPriceOnDiffServer,differentServer,Price(10,10,10),northPackWithPriceOnDiffServer.creationLocation))
        packRepository.save(Pack(westLocation,"ANY_NAME_10","ANY_DESC_10"))
            .apply { recipeRepository.save(Recipe(this,1,materials.map { CraftingMaterial(1,it) }.toMutableSet())) }//west pack without price should not be included in the resul
        packRepository.save(Pack(eastLocation,"ANY_NAME_11","ANY_DESC_11"))
            .apply { recipeRepository.save(Recipe(this,1,materials.map { CraftingMaterial(1,it) }.toMutableSet())) } //east pack without price should not be included in the resul
        packRepository.save(Pack(northLocation,"ANY_NAME_12","ANY_DESC_12"))
            .apply { recipeRepository.save(Recipe(this,1,materials.map { CraftingMaterial(1,it) }.toMutableSet())) } //north pack without price should not be included in the resul
        packRepository.save(Pack(westLocation,"ANY_NAME_13","ANY_DESC_13"))
            .apply { itemPriceRepository.save(ItemPrice(this,archeageServer,Price(1,1,1))) } //west pack without recipe should not be included in the resul
        packRepository.save(Pack(eastLocation,"ANY_NAME_14","ANY_DESC_14"))
            .apply { itemPriceRepository.save(ItemPrice(this,archeageServer,Price(1,1,1))) } //east pack without recipe should not be included in the resul
        packRepository.save(Pack(northLocation,"ANY_NAME_15","ANY_DESC_15"))
            .apply { itemPriceRepository.save(ItemPrice(this,archeageServer,Price(1,1,1))) } //north pack without recipe should not be included in the resul
        testEntityManager.flush()
    }


}