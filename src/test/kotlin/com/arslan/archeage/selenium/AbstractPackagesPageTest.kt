package com.arslan.archeage.selenium

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Category
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.pageobjects.PackagesPageObject
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance

abstract class AbstractPackagesPageTest : SeleniumTest(){
    protected lateinit var page: PackagesPageObject

    protected lateinit var archeageServer: ArcheageServer
    protected lateinit var anotherArcheageServer: ArcheageServer

    protected lateinit var eastDepartureLocation: Location
    protected lateinit var westDepartureLocation: Location
    protected lateinit var anotherEastDepartureLocation: Location

    protected lateinit var westDestinationLocation: Location
    protected lateinit var eastDestinationLocation: Location
    protected lateinit var anotherEastDestinationLocation: Location

    protected lateinit var anotherArcheageServerEastDepartureLocation: Location
    protected lateinit var anotherArcheageServerEastDestinationLocation: Location

    protected lateinit var category: Category
    protected lateinit var anotherCategory: Category
    protected lateinit var anotherArcheageServerCategory: Category

    private val packsSortedByProfit = mutableListOf<Pair<Pack,PackDTO>>()
    //all collections are sorted by profit
    protected lateinit var archeageServerEastPacks: List<PackDTO>
    protected lateinit var anotherArcheageServerEastPacks: List<PackDTO>
    protected lateinit var archeageServerEastDepartureLocationPacks : List<PackDTO>
    protected lateinit var archeageServerEastOtherDepartureLocationPacks : List<PackDTO>
    protected lateinit var archeageServerDestinationLocationPacks: List<PackDTO>
    protected lateinit var archeageServerOtherDestinationLocationPacks: List<PackDTO>
    protected lateinit var archeageServerEastCategoryPacks: List<PackDTO>
    protected lateinit var archeageServerEastOtherCategoryPacks: List<PackDTO>
    protected lateinit var archeageServerWestPacks: List<PackDTO>

    @BeforeEach
    override fun setUp() {
        super.setUp()
        archeageServer = createArcheageServer("SOME_ARCHEAGE_SERVER")
        anotherArcheageServer = createArcheageServer("ANOTHER_ARCHEAGE_SERVER")

        eastDepartureLocation = createEastDepartureLocation("SOME_EAST_CREATE_LOCATION_1", archeageServer)
        anotherEastDepartureLocation = createEastDepartureLocation("SOME_EAST_CREATE_LOCATION_2", archeageServer)
        anotherArcheageServerEastDepartureLocation =
            createEastDepartureLocation("ANOTHER_SERVER_EAST_LOCATION", anotherArcheageServer)
        westDepartureLocation = createWestDepartureLocation("SOME_WEST_CREATE_LOCATION_1", archeageServer)

        eastDestinationLocation = createEastDestinationLocation("SOME_EAST_SELL_LOCATION_1", archeageServer)
        anotherEastDestinationLocation = createEastDestinationLocation("SOME_EAST_SELL_LOCATION_2", archeageServer)
        anotherArcheageServerEastDestinationLocation =
            createEastDestinationLocation("ANOTHER_SERVER_EAST_SELL_LOCATION", anotherArcheageServer)
        westDestinationLocation = createWestDestinationLocation("SOME_WEST_SELL_LOCATION_1", archeageServer)

        val someCategory = createCategory("SOME_CATEGORY",archeageServer)
        category = createCategory("SOME_CATEGORY_1", archeageServer)
        anotherCategory = createCategory("SOME_CATEGORY_2", archeageServer)
        anotherArcheageServerCategory = createCategory("ANOTHER_ARCHEAGE_SERVER_CATEGORY", anotherArcheageServer)

        page = PackagesPageObject(webDriver, port).get()

        //cost is going to be static for all packs - 1 gold.
        //net profit will decrease by 10 for each pack.
        //working points profit will increase for each pack by 10 gold.
        var price = Price(1011, 0, 0)
        var targetWorkingPointsProfit = Price(0, 0, 0)

        repeat(10) {
            price -= Price(10, 0, 0)
            targetWorkingPointsProfit += Price(10, 0, 0)
            val workingPoints = (price - Price(1, 0, 0)) / targetWorkingPointsProfit
            val materials = listOf(Triple("MATERIAL_${it}", Price(0, 50, 0), 1))

            createPack("SOME_PACK_${it}", eastDepartureLocation, eastDestinationLocation, price, 1, someCategory, materials, workingPoints).also { (pack,packDTO) ->
                packsSortedByProfit.add(pack to packDTO)
            }
        }

        repeat(10) {
            price -= Price(10, 0, 0)
            targetWorkingPointsProfit += Price(10, 0, 0)
            val workingPoints = (price - Price(1, 0, 0)) / targetWorkingPointsProfit
            val materials = listOf(Triple("MATERIAL_${it+10}", Price(0, 50, 0), 1))

            createPack("SOME_PACK_${it + 20}", westDepartureLocation, westDestinationLocation, price, 1, someCategory, materials, workingPoints).also { (pack,packDTO) ->
                packsSortedByProfit.add(pack to packDTO)
            }
        }

        repeat(10){
            price -= Price(10, 0, 0)
            targetWorkingPointsProfit += Price(10, 0, 0)
            val workingPoints = (price - Price(1, 0, 0)) / targetWorkingPointsProfit
            val materials = listOf(Triple("MATERIAL_${it+30}", Price(0, 50, 0), 1))
            createPack("SOME_PACK_${it+30}",anotherArcheageServerEastDepartureLocation,anotherArcheageServerEastDestinationLocation,price,1,anotherArcheageServerCategory,materials,workingPoints).also { (pack,packDTO) ->
                packsSortedByProfit.add(pack to packDTO)
            }
        }

        repeat(10) {
            price -= Price(10, 0, 0)
            targetWorkingPointsProfit += Price(10, 0, 0)
            val workingPoints = (price - Price(1, 0, 0)) / targetWorkingPointsProfit
            val materials = listOf(Triple("MATERIAL_${it+40}", Price(0, 50, 0), 1))

            createPack("SOME_PACK_${it + 10}", anotherEastDepartureLocation, anotherEastDestinationLocation, price, 1, category,materials, workingPoints).also { (pack,packDTO) ->
                packsSortedByProfit.add(pack to packDTO)
            }
        }

        repeat(10) {
            price -= Price(10, 0, 0)
            targetWorkingPointsProfit += Price(10, 0, 0)
            val workingPoints = (price - Price(1, 0, 0)) / targetWorkingPointsProfit
            val materials = listOf(Triple("MATERIAL_${it+40}", Price(0, 50, 0), 1))

            createPack("SOME_PACK_${it + 10}", anotherEastDepartureLocation, anotherEastDestinationLocation, price, 1, anotherCategory,materials, workingPoints).also { (pack,packDTO) ->
                packsSortedByProfit.add(pack to packDTO)
            }
        }
        packsSortedByProfit.sortByDescending { it.second.profit }
        archeageServerEastPacks = packsSortedByProfit.filter { it.first.archeageServer == archeageServer && it.first.creationLocation.continent == Continent.EAST }.map { it.second }
        anotherArcheageServerEastPacks = packsSortedByProfit.filter { it.first.archeageServer == anotherArcheageServer && it.first.creationLocation.continent == Continent.EAST }.map { it.second }
        archeageServerEastDepartureLocationPacks = packsSortedByProfit.filter { (pack,_) -> pack.creationLocation == eastDepartureLocation }.map{ it.second }
        archeageServerEastOtherDepartureLocationPacks = packsSortedByProfit.filter { (pack,_) -> pack.creationLocation == anotherEastDepartureLocation }.map{ it.second }
        archeageServerDestinationLocationPacks = packsSortedByProfit.filter { (pack,_) -> pack.price.sellLocation == eastDestinationLocation }.map { it.second }
        archeageServerOtherDestinationLocationPacks = packsSortedByProfit.filter { (pack,_) -> pack.price.sellLocation == anotherEastDestinationLocation }.map { it.second }
        archeageServerEastCategoryPacks = packsSortedByProfit.filter{ (pack,_) -> pack.archeageServer == archeageServer && pack.category == category && pack.creationLocation.continent == Continent.EAST }.map { it.second }
        archeageServerEastOtherCategoryPacks = packsSortedByProfit.filter{ (pack,_) -> pack.archeageServer == archeageServer && pack.category == anotherCategory && pack.creationLocation.continent == Continent.EAST }.map { it.second }
        archeageServerWestPacks = packsSortedByProfit.filter { (pack,_) -> pack.archeageServer == archeageServer && pack.creationLocation.continent == Continent.WEST }.map { it.second }
    }
    protected fun Collection<PackDTO>.sortByWorkingPointsProfit() = sortedByDescending(PackDTO::workingPointsProfit)

}