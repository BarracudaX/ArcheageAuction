package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.Recipe
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
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
 * the passed archeage server - specifically, it returns packs that belong to the server.
 * Finally, all methods returning pack return them sorted by profit.
 */
class PackServiceITest(
    private val packService: PackService,
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

    private val materials = mutableListOf<Item>()

    private val materialsOfAnotherArcheageServer = mutableListOf<Item>()

    private val materialPrices = mutableMapOf<Long,UserPrice>()


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
        secondNorthLocation = locationRepository.save(Location(
            "ANY_NAME_31231233",
            Continent.NORTH,
            archeageServer,
            true
        ))
        thirdWestLocation = locationRepository.save(Location(
            "THIRD_WEST_LOCATION",
            Continent.WEST,
            archeageServer,
            true
        ))
        thirdNorthLocation = locationRepository.save(Location(
            "THIRD_NORTH_LOCATION",
            Continent.NORTH,
            archeageServer,
            true
        ))
        thirdEastLocation = locationRepository.save(Location(
            "THIRD_EAST_LOCATION",
            Continent.EAST,
            archeageServer,
            true
        ))
        materials.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_1","MATERIAL_1",archeageServer)).apply {
            val price = userPriceRepository.save(
                UserPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user)
            )
            materialPrices[id!!] = price
        })
        materials.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_2","MATERIAL_2",archeageServer)).apply {
            val price = userPriceRepository.save(UserPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user))
            materialPrices[id!!] = price
        })
        materials.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_3","MATERIAL_3",archeageServer)).apply {
            val price = userPriceRepository.save(UserPrice(this,archeageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user))
            materialPrices[id!!] = price
        })
        materialsOfAnotherArcheageServer.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_1","MATERIAL_1",anotherArcheageServer)).apply {
            val price = userPriceRepository.save(
                UserPrice(this,anotherArcheageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user)
            )
            materialPrices[id!!] = price
        })
        materialsOfAnotherArcheageServer.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_2","MATERIAL_2",anotherArcheageServer)).apply {
            val price = userPriceRepository.save(UserPrice(this,anotherArcheageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user))
            materialPrices[id!!] = price
        })
        materialsOfAnotherArcheageServer.add(purchasableItemRepository.save(PurchasableItem("MATERIAL_3","MATERIAL_3",anotherArcheageServer)).apply {
            val price = userPriceRepository.save(UserPrice(this,anotherArcheageServer,Price(Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE),Random.nextInt(0,Int.MAX_VALUE)),user))
            materialPrices[id!!] = price
        })
    }

    @AfterEach
    fun clear(){
        ArcheageServerContextHolder.clear()
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return empty list when trying to retrieve packs of continent which does not have any packs`(continent: Continent) {
        if(continent == westLocation.continent) return

        val pack = packRepository.save(Pack(westLocation, "ANY_NAME","ANY_DESC"))
        val packPrice = PackPrice(Price(1,1,1),secondWestLocation)
        pack.addPrice(packPrice)

        packService.packs(continent,archeageServer).shouldBeEmpty()
        packService.packs(continent,pack.creationLocation.id!!,packPrice.sellLocation.id!!,archeageServer).shouldBeEmpty()
        packService.packsCreatedAt(continent,pack.creationLocation.id!!,archeageServer).shouldBeEmpty()
        packService.packsSoldAt(continent,packPrice.sellLocation.id!!,archeageServer).shouldBeEmpty()
    }


    @Test
    fun `should only return packs of requested continent that have at least one price and one recipe at the currently specified server sorted by profit`() {
        preparePacksThatShouldNotBeIncludedInTheResult()

        val expectedWestPacks = packRepository.saveAll(listOf(
            Pack(westLocation, "ANY_NAME_1","ANY_DESC_1"),
            Pack(westLocation, "ANY_NAME_2","ANY_DESC_2")
        ))
        val expectedEastPacks = packRepository.saveAll(listOf(
            Pack(eastLocation, "ANY_NAME_3","ANY_DESC_3"),
            Pack(eastLocation, "ANY_NAME_4","ANY_DESC_4")
        ))
        val expectedNorthPacks = packRepository.saveAll(listOf(
            Pack(northLocation, "ANY_NAME_5","ANY_DESC_5"),
            Pack(northLocation, "ANY_NAME_6","ANY_DESC_6")
        ))

       makePricesAndRecipesFor(expectedEastPacks.plus(expectedWestPacks).plus(expectedNorthPacks))

        packService.packs(Continent.WEST,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedWestPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.EAST,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedEastPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.NORTH,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedNorthPacks.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    @Test
    fun `should only return packs of requested continent that have the provided departure location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDepartureLocation()

        val expectedWestLocations = packRepository.saveAll(listOf(
            Pack(westLocation, "ANY_PACK_NAME_2312","ANY_DESC"),
            Pack(westLocation, "ANY_PACK_NAME_1231232","ANY_DESC")
        ))
        val expectedEastLocations = packRepository.saveAll(listOf(
            Pack(eastLocation, "ANY_PACK_NAME_4324","ANY_DESC"),
            Pack(eastLocation, "ANY_PACK_NAME_215123","ANY_DESC")
        ))
        val expectedNorthLocations = packRepository.saveAll(listOf(
            Pack(northLocation, "ANY_PACK_NAME_34093","ANY_DESC"),
            Pack(northLocation, "ANY_PACK_NAME_09433","ANY_DESC")
        ))

        makePricesAndRecipesFor(expectedEastLocations.plus(expectedWestLocations).plus(expectedNorthLocations))

        packService.packsCreatedAt(Continent.WEST,westLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedWestLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsCreatedAt(Continent.EAST,eastLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedEastLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsCreatedAt(Continent.NORTH,northLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedNorthLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    /**
     * Destination location = sell location.
     */
    @Test
    fun `should only return packs of requested continent that have the provided destination location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDestinationLocation()

        val expectedWestLocations = packRepository.saveAll(listOf(
            Pack(westLocation, "ANY_PACK_NAME_290923","ANY_DESC"),
            Pack(thirdWestLocation, "ANY_PACK_NAME_12311","ANY_DESC")
        )).map { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondWestLocation))
            pack
        }
        val expectedEastLocations = packRepository.saveAll(listOf(
            Pack(eastLocation, "ANY_PACK_NAME_34893","ANY_DESC"),
            Pack(thirdEastLocation, "ANY_PACK_NAME_231123","ANY_DESC")
        )).map { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondEastLocation))
            pack
        }
        val expectedNorthLocations = packRepository.saveAll(listOf(
            Pack(northLocation, "ANY_PACK_NAME_090123","ANY_DESC"),
            Pack(thirdNorthLocation, "ANY_PACK_NAME_909213","ANY_DESC")
        )).map { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondNorthLocation))
            pack
        }

        packService.packsSoldAt(Continent.WEST,secondWestLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedWestLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsSoldAt(Continent.EAST,secondEastLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedEastLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packsSoldAt(Continent.NORTH,secondNorthLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedNorthLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    @Test
    fun `should return packs created at specified location and sold at specified location`() {
        preparePacksThatShouldNotBeIncludedInTheResult()
        preparePacksWithRandomDepartureLocation()
        preparePacksWithRandomDestinationLocation()

        val expectedWestLocations = packRepository.saveAll(listOf(
            Pack(westLocation, "ANY_PACK_NAME_87244","ANY_DESC"),
            Pack(westLocation, "ANY_PACK_NAME_123781","ANY_DESC")
        )).map { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondWestLocation))
            pack
        }
        val expectedEastLocations = packRepository.saveAll(listOf(
            Pack(eastLocation, "ANY_PACK_NAME_39489143","ANY_DESC"),
            Pack(eastLocation, "ANY_PACK_NAME_13874873","ANY_DESC")
        )).map { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondEastLocation))
            pack
        }
        val expectedNorthLocations = packRepository.saveAll(listOf(
            Pack(northLocation, "ANY_PACK_NAME_49812315","ANY_DESC"),
            Pack(northLocation, "ANY_PACK_NAME_437887823","ANY_DESC")
        )).map { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondNorthLocation))
            pack
        }

        packService.packs(Continent.WEST,westLocation.id!!,secondWestLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedWestLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.EAST,eastLocation.id!!,secondEastLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedEastLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
        packService.packs(Continent.NORTH,northLocation.id!!,secondNorthLocation.id!!,archeageServer).shouldNotBeEmpty().shouldContainExactly(expectedNorthLocations.toDTO(materialPrices).sortedByDescending(PackDTO::profit))
    }

    private fun makePricesAndRecipesFor(packs: List<Pack>){
        for(pack in packs){
            pack.addPrice(PackPrice(Price(1,1,1), pack.creationLocation))
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
        }
    }

    private fun preparePacksWithRandomDestinationLocation(){
        val randomWestLocation = locationRepository.save(Location(
            "ANY_LOCATION_NAME_1",
            Continent.WEST,
            archeageServer,
            true
        ))
        val randomEastLocation =  locationRepository.save(Location(
            "ANY_LOCATION_NAME_2",
            Continent.EAST,
            archeageServer,
            true
        ))
        val randomNorthLocation =  locationRepository.save(Location(
            "ANY_LOCATION_NAME_2",
            Continent.NORTH,
            archeageServer,
            true
        ))

        packRepository.save(Pack(westLocation, "ANY_NAME_232","ANY_DESC")).let { pack ->
           recipeRepository.save(Recipe(pack,1)).also {recipe ->
               materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
           }
            pack.addPrice(PackPrice(Price(1,1,1), randomWestLocation))
        }
        packRepository.save(Pack(eastLocation, "ANY_NAME_5234","ANY_DESC")).let { pack ->
           recipeRepository.save(Recipe(pack,1)).also {recipe ->
               materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
           }
            pack.addPrice(PackPrice(Price(1,1,1), randomEastLocation))
        }
        packRepository.save(Pack(northLocation, "ANY_NAME_123512","ANY_DESC")).let { pack ->
           recipeRepository.save(Recipe(pack,1)).also {recipe ->
               materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
           }
            pack.addPrice(PackPrice(Price(1,1,1), randomNorthLocation))
        }
    }

    private fun preparePacksWithRandomDepartureLocation(){
        val randomWestLocation = locationRepository.save(Location("ANY_LOCATION_NAME_1",Continent.WEST,archeageServer))
        val randomEastLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.EAST,archeageServer))
        val randomNorthLocation =  locationRepository.save(Location("ANY_LOCATION_NAME_2",Continent.NORTH,archeageServer))

        packRepository.save(Pack(randomWestLocation, "ANY_NAME_4309043","ANY_DESC")).let { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1),secondWestLocation))
        }
        packRepository.save(Pack(randomEastLocation, "ANY_NAME_313414","ANY_DESC")).let { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondEastLocation))
        }
        packRepository.save(Pack(randomNorthLocation, "ANY_NAME_12354234","ANY_DESC")).let { pack ->
            recipeRepository.save(Recipe(pack,1)).also {recipe ->
                materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
            }
            pack.addPrice(PackPrice(Price(1,1,1), secondNorthLocation))
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
        val differentServer = archeageServerRepository.save(ArcheageServer("DIFFERENT_SERVER"))
        val westPackWithPriceOnDiffServer = packRepository.save(Pack(westLocationOfDifferentServer, "ANY_NAME_7","ANY_DESC_7")) //west pack on different server
        val eastPackWithPriceOnDiffServer = packRepository.save(Pack(eastLocationOfDifferentServer, "ANY_NAME_8","ANY_DESC_8")) //east pack on different server
        val northPackWithPriceOnDiffServer = packRepository.save(Pack(northLocationOfDifferentServer, "ANY_NAME_9","ANY_DESC_9")) //north pack with price on different server
        recipeRepository.save(Recipe(westPackWithPriceOnDiffServer,1)).also {recipe ->
            materialsOfAnotherArcheageServer.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
        }
        recipeRepository.save(Recipe(eastPackWithPriceOnDiffServer,1)).also {recipe ->
            materialsOfAnotherArcheageServer.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
        }
        recipeRepository.save(Recipe(northPackWithPriceOnDiffServer,1)).also {recipe ->
            materialsOfAnotherArcheageServer.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) }
        }
        westPackWithPriceOnDiffServer.addPrice(PackPrice(Price(1,1,1), westPackWithPriceOnDiffServer.creationLocation))
        eastPackWithPriceOnDiffServer.addPrice(PackPrice(Price(1,1,1), eastPackWithPriceOnDiffServer.creationLocation))
        northPackWithPriceOnDiffServer.addPrice(PackPrice(Price(1,1,1), northPackWithPriceOnDiffServer.creationLocation))
        packRepository.save(Pack(westLocation, "ANY_NAME_10","ANY_DESC_10"))
            .apply {
                recipeRepository.save(Recipe(this,1)).also {recipe -> materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) } }
            }//west pack without price should not be included in the resul
        packRepository.save(Pack(eastLocation, "ANY_NAME_11","ANY_DESC_11"))
            .apply {
                recipeRepository.save(Recipe(this,1)).also {recipe -> materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) } }
            } //east pack without price should not be included in the resul
        packRepository.save(Pack(northLocation, "ANY_NAME_12","ANY_DESC_12"))
            .apply { recipeRepository.save(Recipe(this,1)).also {recipe -> materials.forEach { recipe.addMaterial(CraftingMaterial(Random.nextInt(0,10),it)) } } } //north pack without price should not be included in the resul
        packRepository.save(Pack(westLocation, "ANY_NAME_13","ANY_DESC_13"))
            .apply { addPrice(PackPrice(Price(1,1,1), secondWestLocation)) } //west pack without recipe should not be included in the resul
        packRepository.save(Pack(eastLocation, "ANY_NAME_14","ANY_DESC_14"))
            .apply { addPrice(PackPrice(Price(1,1,1), secondEastLocation)) } //east pack without recipe should not be included in the resul
        packRepository.save(Pack(northLocation, "ANY_NAME_15","ANY_DESC_15"))
            .apply { addPrice(PackPrice(Price(1,1,1), secondNorthLocation)) } //north pack without recipe should not be included in the resul
        testEntityManager.flush()
    }


}