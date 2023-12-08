package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.ItemRecipe
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.entity.pack.PackRecipe
import com.arslan.archeage.toDTO
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.random.Random

/**
 * All PackService methods that return packs, return only those packs that have at least one price and at least one recipe.Also, pack service returns packs based on
 * the current value that is held in ArcheageServerContextHolder - specifically, it returns packs that belong to the server of the held value.
 * Finally, all methods returning pack return them sorted by profit.
 */
class PackServiceITest(
    private val packService: PackService,
) : AbstractITest() {

    private lateinit var westLocation: Location

    private lateinit var secondWestLocation: Location

    private lateinit var thirdWestLocation: Location

    private lateinit var eastLocation: Location

    private lateinit var secondEastLocation: Location

    private lateinit var thirdEastLocation: Location

    private lateinit var northLocation: Location

    private lateinit var secondNorthLocation: Location

    private lateinit var thirdNorthLocation: Location

    private lateinit var archeageServer: ArcheageServer

    private lateinit var user: User

    private val materials = mutableListOf<Item>()

    private val materialPrices = mutableMapOf<Long,UserPrice>()


    @BeforeEach
    fun prepareTestContext(){
        user = userRepository.save(User("ANY_EMAIL","ANY_PASSWORD"))
        westLocation = locationRepository.save(Location("ANY_NAME_1",Continent.WEST, Region.EUROPE,true))
        eastLocation = locationRepository.save(Location("ANY_NAME_2",Continent.EAST,Region.EUROPE,true))
        northLocation = locationRepository.save(Location("ANY_NAME_3",Continent.NORTH,Region.EUROPE,true))
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY_NAME",Region.EUROPE))
        secondWestLocation = locationRepository.save(Location("ANY_NAME_4",Continent.WEST,Region.EUROPE,true))
        secondEastLocation = locationRepository.save(Location("ANY_NAME_123123",Continent.EAST,Region.EUROPE,true))
        secondNorthLocation = locationRepository.save(Location("ANY_NAME_31231233",Continent.NORTH,Region.EUROPE,true))
        thirdWestLocation = locationRepository.save(Location("THIRD_WEST_LOCATION",Continent.WEST,Region.EUROPE,true))
        thirdNorthLocation = locationRepository.save(Location("THIRD_NORTH_LOCATION",Continent.NORTH,Region.EUROPE,true))
        thirdEastLocation = locationRepository.save(Location("THIRD_EAST_LOCATION",Continent.EAST,Region.EUROPE,true))
        ArcheageServerContextHolder.setServerContext(archeageServer)
        materials.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_1","MATERIAL_1",Region.EUROPE, mutableSetOf())).apply {
            val price = userPriceRepository.save(
                UserPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user)
            )
            prices.add(price)
            materialPrices[id!!] = price
        })
        materials.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_2","MATERIAL_2",Region.EUROPE, mutableSetOf())).apply {
            val price = userPriceRepository.save(UserPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user))
            prices.add(price)
            materialPrices[id!!] = price
        })
        materials.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_3","MATERIAL_3",Region.EUROPE, mutableSetOf())).apply {
            val price = userPriceRepository.save(UserPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user))
            prices.add(price)
            materialPrices[id!!] = price
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
        val packPrice = PackPrice(archeageServer,Price(1,1,1),secondWestLocation)
        pack.prices.add(packPrice)

        packService.packs(pack.creationLocation.continent).shouldBeEmpty()
        packService.packs(pack.creationLocation.continent,pack.creationLocation.name,packPrice.sellLocation.name).shouldBeEmpty()
        packService.packsCreatedAt(pack.creationLocation.continent,pack.creationLocation.name).shouldBeEmpty()
        packService.packsSoldAt(pack.creationLocation.continent,packPrice.sellLocation.name).shouldBeEmpty()
    }


    @MethodSource("continents")
    @ParameterizedTest
    fun `should return empty list when trying to retrieve packs of continent which does not have any packs`(continent: Continent) {
        if(continent == westLocation.continent) return

        val pack = packRepository.save(Pack(westLocation,"ANY_NAME","ANY_DESC"))
        val packPrice = PackPrice(archeageServer, Price(1,1,1),secondWestLocation)
        pack.prices.add(packPrice)

        packService.packs(continent).shouldBeEmpty()
        packService.packs(continent,pack.creationLocation.name,packPrice.sellLocation.name).shouldBeEmpty()
        packService.packsCreatedAt(continent,pack.creationLocation.name).shouldBeEmpty()
        packService.packsSoldAt(continent,packPrice.sellLocation.name).shouldBeEmpty()
    }


    @Test
    fun `should only return packs of requested continent that have at least one price and one recipe at the currently specified server sorted by profit`() {
        preparePacksThatShouldNotBeIncludedInTheResult()

        val expectedWestPacks = packRepository.saveAll(listOf(
            Pack(westLocation,"ANY_NAME_1","ANY_DESC_1"),
            Pack(westLocation,"ANY_NAME_2","ANY_DESC_2")
        ))
        val expectedEastPacks = packRepository.saveAll(listOf(
            Pack(eastLocation,"ANY_NAME_3","ANY_DESC_3"),
            Pack(eastLocation,"ANY_NAME_4","ANY_DESC_4")
        ))
        val expectedNorthPacks = packRepository.saveAll(listOf(
            Pack(northLocation,"ANY_NAME_5","ANY_DESC_5"),
            Pack(northLocation,"ANY_NAME_6","ANY_DESC_6")
        ))

       makePricesAndRecipesFor(expectedEastPacks.plus(expectedWestPacks).plus(expectedNorthPacks))

        packService.packs(Continent.WEST).shouldNotBeEmpty().shouldContainExactly(expectedWestPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.EAST).shouldNotBeEmpty().shouldContainExactly(expectedEastPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.NORTH).shouldNotBeEmpty().shouldContainExactly(expectedNorthPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    @Test
    fun `should only return packs of requested continent that have the provided departure location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDepartureLocation()

        val expectedWestLocations = packRepository.saveAll(listOf(
            Pack(westLocation,"ANY_PACK_NAME_2312","ANY_DESC"),
            Pack(westLocation,"ANY_PACK_NAME_1231232","ANY_DESC")
        ))
        val expectedEastLocations = packRepository.saveAll(listOf(
            Pack(eastLocation,"ANY_PACK_NAME_4324","ANY_DESC"),
            Pack(eastLocation,"ANY_PACK_NAME_215123","ANY_DESC")
        ))
        val expectedNorthLocations = packRepository.saveAll(listOf(
            Pack(northLocation,"ANY_PACK_NAME_34093","ANY_DESC"),
            Pack(northLocation,"ANY_PACK_NAME_09433","ANY_DESC")
        ))

        makePricesAndRecipesFor(expectedEastLocations.plus(expectedWestLocations).plus(expectedNorthLocations))

        packService.packsCreatedAt(Continent.WEST,westLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedWestLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsCreatedAt(Continent.EAST,eastLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedEastLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsCreatedAt(Continent.NORTH,northLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedNorthLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    /**
     * Destination location = sell location.
     */
    @Test
    fun `should only return packs of requested continent that have the provided destination location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDestinationLocation()

        val expectedWestLocations = packRepository.saveAll(listOf(
            Pack(westLocation,"ANY_PACK_NAME_290923","ANY_DESC"),
            Pack(thirdWestLocation,"ANY_PACK_NAME_12311","ANY_DESC")
        )).map { pack ->
            packRecipeRepository.save(PackRecipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondWestLocation))
            pack
        }
        val expectedEastLocations = packRepository.saveAll(listOf(
            Pack(eastLocation,"ANY_PACK_NAME_34893","ANY_DESC"),
            Pack(thirdEastLocation,"ANY_PACK_NAME_231123","ANY_DESC")
        )).map { pack ->
            packRecipeRepository.save(PackRecipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondEastLocation))
            pack
        }
        val expectedNorthLocations = packRepository.saveAll(listOf(
            Pack(northLocation,"ANY_PACK_NAME_090123","ANY_DESC"),
            Pack(thirdNorthLocation,"ANY_PACK_NAME_909213","ANY_DESC")
        )).map { pack ->
            packRecipeRepository.save(PackRecipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondNorthLocation))
            pack
        }

        packService.packsSoldAt(Continent.WEST,secondWestLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedWestLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsSoldAt(Continent.EAST,secondEastLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedEastLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsSoldAt(Continent.NORTH,secondNorthLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedNorthLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    @Test
    fun `should return packs created at specified location and sold at specified location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDepartureLocation()
        preparePacksWithRandomDestinationLocation()

        val expectedWestLocations = packRepository.saveAll(listOf(
            Pack(westLocation,"ANY_PACK_NAME_87244","ANY_DESC"),
            Pack(westLocation,"ANY_PACK_NAME_123781","ANY_DESC")
        )).map { pack ->
            packRecipeRepository.save(PackRecipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondWestLocation))
            pack
        }
        val expectedEastLocations = packRepository.saveAll(listOf(
            Pack(eastLocation,"ANY_PACK_NAME_39489143","ANY_DESC"),
            Pack(eastLocation,"ANY_PACK_NAME_13874873","ANY_DESC")
        )).map { pack ->
            packRecipeRepository.save(PackRecipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondEastLocation))
            pack
        }
        val expectedNorthLocations = packRepository.saveAll(listOf(
            Pack(northLocation,"ANY_PACK_NAME_49812315","ANY_DESC"),
            Pack(northLocation,"ANY_PACK_NAME_437887823","ANY_DESC")
        )).map { pack ->
            packRecipeRepository.save(PackRecipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondNorthLocation))
            pack
        }

        packService.packs(Continent.WEST,westLocation.name,secondWestLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedWestLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.EAST,eastLocation.name,secondEastLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedEastLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.NORTH,northLocation.name,secondNorthLocation.name).shouldNotBeEmpty().shouldContainExactly(expectedNorthLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    private fun makePricesAndRecipesFor(packs: List<Pack>){
        for(pack in packs){
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), pack.creationLocation))
            packRecipeRepository.save(PackRecipe(pack,1, materials.map { CraftingMaterial(Random.nextInt(0,10),it) }.toMutableSet()))
        }
    }

    private fun preparePacksWithRandomDestinationLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1",Continent.WEST,Region.EUROPE,true))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.EAST,Region.EUROPE,true))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.NORTH,Region.EUROPE,true))

        packRepository.save(Pack(westLocation,"ANY_NAME_232","ANY_DESC")).let { pack ->
           packRecipeRepository.save(PackRecipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), randomWestLocation))
        }
        packRepository.save(Pack(eastLocation,"ANY_NAME_5234","ANY_DESC")).let { pack ->
           packRecipeRepository.save(PackRecipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), randomEastLocation))
        }
        packRepository.save(Pack(northLocation,"ANY_NAME_123512","ANY_DESC")).let { pack ->
           packRecipeRepository.save(PackRecipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), randomNorthLocation))
        }
    }

    private fun preparePacksWithRandomDepartureLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1",Continent.WEST,Region.EUROPE))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.EAST,Region.EUROPE))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.NORTH,Region.EUROPE))

        packRepository.save(Pack(randomWestLocation,"ANY_NAME_4309043","ANY_DESC")).let { pack ->
            packRecipeRepository.save(PackRecipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer, Price(1,1,1),secondWestLocation))
        }
        packRepository.save(Pack(randomEastLocation,"ANY_NAME_313414","ANY_DESC")).let { pack ->
            packRecipeRepository.save(PackRecipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondEastLocation))
        }
        packRepository.save(Pack(randomNorthLocation,"ANY_NAME_12354234","ANY_DESC")).let { pack ->
            packRecipeRepository.save(PackRecipe(pack,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
            pack.prices.add(PackPrice(archeageServer,Price(1,1,1), secondNorthLocation))
        }
        testEntityManager.flush()
    }

    /**
     * These packs are excluded by default for one of the following reasons:
     * a)They have recipe and price, but price belongs to different server from the default server used in tests.
     * b)They have recipe but not a price.
     * c)They have price but not a recipe.
     */
    private fun preparePacksThatShouldNotBeIncludedInTheResult(){
        //packs that have price for different server should not be included if user's chosen server is different.
        //packs that do not have any price, should not be returned.
        val differentServer = archeageServerRepository.save(ArcheageServer("DIFFERENT_SERVER",Region.EUROPE))
        val westPackWithPriceOnDiffServer = packRepository.save(Pack(westLocation,"ANY_NAME_7","ANY_DESC_7")) //west pack with price on different server
        val eastPackWithPriceOnDiffServer = packRepository.save(Pack(eastLocation,"ANY_NAME_8","ANY_DESC_8")) //east pack with price on different server
        val northPackWithPriceOnDiffServer = packRepository.save(Pack(northLocation,"ANY_NAME_9","ANY_DESC_9")) //north pack with price on different server
        packRecipeRepository.save(PackRecipe(westPackWithPriceOnDiffServer,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
        packRecipeRepository.save(PackRecipe(eastPackWithPriceOnDiffServer,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
        packRecipeRepository.save(PackRecipe(northPackWithPriceOnDiffServer,1,materials.map { CraftingMaterial(1,it) }.toMutableSet()))
        westPackWithPriceOnDiffServer.prices.add(PackPrice(differentServer,Price(1,1,1), westPackWithPriceOnDiffServer.creationLocation))
        eastPackWithPriceOnDiffServer.prices.add(PackPrice(differentServer,Price(1,1,1), eastPackWithPriceOnDiffServer.creationLocation))
        northPackWithPriceOnDiffServer.prices.add(PackPrice(differentServer,Price(1,1,1), northPackWithPriceOnDiffServer.creationLocation))
        packRepository.save(Pack(westLocation,"ANY_NAME_10","ANY_DESC_10"))
            .apply { packRecipeRepository.save(PackRecipe(this,1,materials.map { CraftingMaterial(1,it) }.toMutableSet())) }//west pack without price should not be included in the resul
        packRepository.save(Pack(eastLocation,"ANY_NAME_11","ANY_DESC_11"))
            .apply { packRecipeRepository.save(PackRecipe(this,1,materials.map { CraftingMaterial(1,it) }.toMutableSet())) } //east pack without price should not be included in the resul
        packRepository.save(Pack(northLocation,"ANY_NAME_12","ANY_DESC_12"))
            .apply { packRecipeRepository.save(PackRecipe(this,1,materials.map { CraftingMaterial(1,it) }.toMutableSet())) } //north pack without price should not be included in the resul
        packRepository.save(Pack(westLocation,"ANY_NAME_13","ANY_DESC_13"))
            .apply { prices.add(PackPrice(archeageServer,Price(1,1,1), secondWestLocation)) } //west pack without recipe should not be included in the resul
        packRepository.save(Pack(eastLocation,"ANY_NAME_14","ANY_DESC_14"))
            .apply { prices.add(PackPrice(archeageServer,Price(1,1,1), secondEastLocation)) } //east pack without recipe should not be included in the resul
        packRepository.save(Pack(northLocation,"ANY_NAME_15","ANY_DESC_15"))
            .apply { prices.add(PackPrice(archeageServer,Price(1,1,1), secondNorthLocation)) } //north pack without recipe should not be included in the resul
        testEntityManager.flush()
    }


}