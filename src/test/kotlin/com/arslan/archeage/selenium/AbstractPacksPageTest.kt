package com.arslan.archeage.selenium

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.pack.Pack
import org.junit.jupiter.api.BeforeEach

abstract class AbstractPacksPageTest : SeleniumTest(){

    protected lateinit var archeageServer: ArcheageServer
    protected lateinit var anotherArcheageServer: ArcheageServer

    protected lateinit var eastDepartureLocation: Location
    protected lateinit var westDepartureLocation: Location

    protected lateinit var westDestinationLocation: Location
    protected lateinit var eastDestinationLocation: Location

    protected lateinit var anotherArcheageServerEastDepartureLocation: Location
    protected lateinit var anotherArcheageServerEastDestinationLocation: Location

    protected lateinit var category: Category
    protected lateinit var anotherCategory: Category
    protected lateinit var anotherArcheageServerCategory: Category

    private val archeageServerPacks = mutableListOf<Pair<Pack,PackDTO>>()
    private val anotherArcheageServerPacks = mutableListOf<Pair<Pack,PackDTO>>()
    //all collections are sorted by profit
    protected lateinit var archeageServerEastPacks: List<PackDTO>
    protected lateinit var archeageServerEastDepartureLocationPacks: List<PackDTO>
    protected lateinit var archeageServerDestinationLocationPacks: List<PackDTO>
    protected lateinit var anotherArcheageServerEastPack: PackDTO
    protected lateinit var archeageServerEastDepartureLocationPack : PackDTO
    protected lateinit var archeageServerEastDestinationLocationPack: PackDTO
    protected lateinit var archeageServerEastOtherCategoryPack: PackDTO
    protected lateinit var archeageServerWestPack: PackDTO
    protected lateinit var user: User
    protected val userPassword = "SomePass123!"

    @BeforeEach
    override fun setUp() {
        super.setUp()
        user = createUser("some@email.com",userPassword)
        archeageServer = createArcheageServer("SOME_ARCHEAGE_SERVER")
        anotherArcheageServer = createArcheageServer("ANOTHER_ARCHEAGE_SERVER")

        eastDepartureLocation = createEastDepartureLocation("SOME_EAST_CREATE_LOCATION_1", archeageServer)
        anotherArcheageServerEastDepartureLocation = createEastDepartureLocation("ANOTHER_SERVER_EAST_LOCATION", anotherArcheageServer)
        westDepartureLocation = createWestDepartureLocation("SOME_WEST_CREATE_LOCATION_1", archeageServer)

        eastDestinationLocation = createEastDestinationLocation("SOME_EAST_SELL_LOCATION_1", archeageServer)
        anotherArcheageServerEastDestinationLocation =
            createEastDestinationLocation("ANOTHER_SERVER_EAST_SELL_LOCATION", anotherArcheageServer)
        westDestinationLocation = createWestDestinationLocation("SOME_WEST_SELL_LOCATION_1", archeageServer)

        val someCategory = createCategory("SOME_CATEGORY",archeageServer)
        category = createCategory("SOME_CATEGORY_1", archeageServer)
        anotherCategory = createCategory("SOME_CATEGORY_2", archeageServer)
        anotherArcheageServerCategory = createCategory("ANOTHER_ARCHEAGE_SERVER_CATEGORY", anotherArcheageServer)

        //cost is going to be static for all packs - 1 gold.
        //net profit will decrease by 10 for each pack.
        //working points profit will increase for each pack by 10 gold.
        var price = Price(1000, 0, 0)
        var targetWorkingPointsProfit = Price(100, 0, 0)
        var workingPoints = 0
        val materials = listOf(Triple("MATERIAL", Price(0, 50, 0), 1))

        repeat(25) {
            price -= Price(10, 0, 0)
            targetWorkingPointsProfit += Price(10, 0, 0)
            workingPoints = price / targetWorkingPointsProfit

            createPack("SOME_PACK_${it}", createEastDepartureLocation("SOME_EAST_CREATE_LOCATION_${it}", archeageServer), createEastDestinationLocation("SOME_EAST_SELL_LOCATION_${it}", archeageServer), price, 1, someCategory, materials, workingPoints,user).also { (pack,packDTO) ->
                archeageServerPacks.add(pack to packDTO)
            }
        }
        price -= Price(10, 0, 0)
        targetWorkingPointsProfit += Price(10, 0, 0)
        workingPoints = price / targetWorkingPointsProfit

        createPack("SOME_WEST_PACK", westDepartureLocation, westDestinationLocation, price, 1, someCategory, materials, workingPoints,user).also { (pack,packDTO) ->
            archeageServerPacks.add(pack to packDTO)
            archeageServerWestPack = packDTO
        }

        price -= Price(10, 0, 0)
        targetWorkingPointsProfit += Price(10, 0, 0)
        workingPoints = price / targetWorkingPointsProfit

        createPack("ANOTHER_SERVER_PACK",anotherArcheageServerEastDepartureLocation,anotherArcheageServerEastDestinationLocation,price,1,anotherArcheageServerCategory,materials,workingPoints,user).also { (pack,packDTO) ->
            anotherArcheageServerPacks.add(pack to packDTO)
            anotherArcheageServerEastPack = packDTO
        }

        price -= Price(10, 0, 0)
        targetWorkingPointsProfit += Price(10, 0, 0)
        workingPoints = price / targetWorkingPointsProfit

        createPack("ANOTHER_DEPARTURE_AND_DESTINATION_LOCATION_PACK", eastDepartureLocation, eastDestinationLocation, price, 1, category,materials, workingPoints,user).also { (pack,packDTO) ->
            archeageServerPacks.add(pack to packDTO)
            archeageServerEastDestinationLocationPack = packDTO
            archeageServerEastDepartureLocationPack=packDTO
        }

        price -= Price(10, 0, 0)
        targetWorkingPointsProfit += Price(10, 0, 0)
        workingPoints = price / targetWorkingPointsProfit

        createPack("ANOTHER_CATEGORY_PACK", createEastDepartureLocation("SOME_EAST_CREATE_LOCATION", archeageServer), createEastDestinationLocation("SOME_EAST_SELL_LOCATION", archeageServer), price, 1, anotherCategory,materials, workingPoints,user).also { (pack,packDTO) ->
            archeageServerPacks.add(pack to packDTO)
            archeageServerEastOtherCategoryPack = packDTO
        }

        archeageServerPacks.sortByDescending { it.second.profit }
        anotherArcheageServerPacks.sortByDescending { it.second.profit }
        archeageServerEastPacks = archeageServerPacks.filter {it.first.creationLocation.continent == Continent.EAST }.map { it.second }
        archeageServerEastDepartureLocationPacks = archeageServerPacks.filter { it.first.creationLocation == eastDepartureLocation }.map { it.second }
        archeageServerDestinationLocationPacks = archeageServerPacks.filter { it.first.price.sellLocation == eastDestinationLocation }.map { it.second }

    }

}