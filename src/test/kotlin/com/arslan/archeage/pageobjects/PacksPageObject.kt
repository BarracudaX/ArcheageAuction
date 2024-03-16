package com.arslan.archeage.pageobjects

import click
import com.arslan.archeage.Continent
import com.arslan.archeage.NoOpCondition
import com.arslan.archeage.PackDTO
import com.arslan.archeage.PackRequest
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Category
import com.arslan.archeage.entity.Location
import com.arslan.archeage.pageobjects.component.*
import com.arslan.archeage.service.PackService
import io.kotest.assertions.fail
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.LoadableComponent
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.retry.support.RetryTemplate
import scrollInto
import java.time.Duration

// page_url = http://localhost:8080/packs_view
open class PacksPageObject(protected val driver: WebDriver, protected val port: Int, private val packService: PackService, private val retryTemplate: RetryTemplate) : LoadableComponent<PacksPageObject>() {

    private val continents = SelectComponent<Continent>(
        "continent",
        driver,{ select -> numberOfElementsToBe(select.optionsBy, Continent.entries.size)},
        { continent -> continent.name },
        retryTemplate
    )
    private val departureLocations = SelectComponent<Location>(
        "departure_location",
        driver,
        { NoOpCondition() },
        { location -> location.id.toString() },
        retryTemplate
    )
    private val destinationLocations = SelectComponent<Location>(
        "destination_location",
        driver,
        { NoOpCondition() },
        { location -> location.id.toString() },
        retryTemplate
    )
    private val pageSelect = SelectComponent<Int>(By.xpath("//select[@name='packs_length']"),By.xpath("//select[@name='packs_length']/option"),driver,{ NoOpCondition() },{ option -> option.toString() },retryTemplate)
    private val categoriesComponent = CategoriesComponent(driver)
    private val paginationComponent = PaginationComponent(driver)
    private val orderComponent = TableOrderComponent(driver,"packs")
    private val errorBy = By.cssSelector("div.alert.alert-danger.alert-dismissible")
    private val packsBy = By.className("pack")
    private var currentPacks = listOf<PackDTO>()
    private var currentArcheageServer: ArcheageServer? = null
    private var currentPage:Int = 0


    override fun load() {
        driver.get("http://localhost:${port}/packs_view")
    }

    override fun isLoaded() {
        val condition = or(presenceOfElementLocated(errorBy), numberOfElementsToBeMoreThan(departureLocations.optionsBy, 0))
        val wait = FluentWait(driver)
            .withTimeout(Duration.ofSeconds(2))
            .ignoring(NoSuchElementException::class.java)
        try{
            wait.until(condition)
            continents.isLoaded()
        }catch (ex: TimeoutException){
            fail("Not Loaded. Reason: ${ex.message}.")
        }
    }

    fun currentPageSize() : Int = pageSelect.selectedValue().toInt()

    fun selectServer(archeageServer: ArcheageServer) : PacksPageObject{
        selectArcheageServer(archeageServer)
        currentArcheageServer = archeageServer
        currentPacks = packs()
        waitForPacks()
        return this
    }


    fun pagination(consumer: (PaginationData) -> Unit) : PacksPageObject{
        paginationComponent.data().apply(consumer)
        return this
    }

    fun selectPage(pageNum: Int) : PacksPageObject{
        checkArcheageServerSelected()
        paginationComponent.selectPage(pageNum)
        currentPage = pageNum - 1
        currentPacks = packs()
        waitForPack(WaitPackStrategy.firstPack(currentPacks[0].id))
        return this
    }


    fun continents() : List<String> = continents.options()

    fun selectedContinent() : Continent = Continent.valueOf(continents.selectedValue())

    fun departureLocations(consumer: (List<String>) -> Unit) : PacksPageObject {
        departureLocations.options().apply(consumer)
        return this
    }

    fun destinationLocations(consumer: (List<String>) -> Unit) : PacksPageObject {
        destinationLocations.options().apply(consumer)

        return this
    }

    fun error(consumer: String?.() -> Unit) : PacksPageObject {
        try{
            driver.findElement(errorBy).text
        }catch (_: NoSuchElementException){
            null
        }.apply(consumer)

        return this
    }

    /**
     * Selects provided continent and waits for the provided location to be loaded before returning.
     */
    fun selectContinent(continent: Continent) : PacksPageObject {
        checkArcheageServerSelected()
        continents.selectValue(continent)
        currentPacks = waitForNewPacks()
        return this
    }

    fun selectDepartureLocation(location: Location) : PacksPageObject{
        checkArcheageServerSelected()
        departureLocations.selectValue(location)
        currentPacks = waitForNewPacks()

        return this
    }

    fun selectDestinationLocation(location: Location) : PacksPageObject{
        checkArcheageServerSelected()
        destinationLocations.selectValue(location)
        currentPacks = waitForNewPacks()

        return this
    }

    fun packs(consumer: (List<PackDTO>) -> Unit) : PacksPageObject{
        val packs = driver.findElements(packsBy).map { pack ->
            PackComponent(driver,pack.getAttribute("id").replace("pack_","").toLong(),retryTemplate)
        }.onEach(PackComponent::isLoaded).map(PackComponent::toPackDTO)

        consumer(packs)

        return this
    }

    fun selectCategory(category: Category) : PacksPageObject{
        checkArcheageServerSelected()
        categoriesComponent.selectCategory(category)
        currentPacks = waitForNewPacks()
        return this
    }

    fun changePageSize(pageSize: Int) : PacksPageObject{
        checkArcheageServerSelected()
        pageSelect.selectValue(pageSize)
        currentPacks = packs()
        waitForPack(WaitPackStrategy.any(currentPacks.last().id))
        return this
    }

    fun changePercentage(packID: Long, newPercentage: Int) {
        PackComponent(driver, packID, retryTemplate).changePercentage(newPercentage)
        waitForNewPacks()
    }

    fun nextPage() : PacksPageObject{
        paginationComponent.nextPage()
        currentPage += 1
        currentPacks = packs()
        waitForPack(WaitPackStrategy.any(currentPacks[0].id))
        return this
    }

    fun lastPage() : PacksPageObject{
        currentPage = paginationComponent.data().paginationNums.maxOfOrNull { it.content.toInt() }!! - 1
        paginationComponent.lastPage()
        currentPacks = packs()
        waitForPack(WaitPackStrategy.any(currentPacks[0].id))
        return this
    }

    fun previousPage() : PacksPageObject{
        paginationComponent.previousPage()
        currentPage -= 1
        currentPacks = packs()
        waitForPack(WaitPackStrategy.any(currentPacks[0].id))
        return this
    }

    fun firstPage() : PacksPageObject {
        paginationComponent.firstPage()
        currentPage = 0
        currentPacks = packs()
        waitForPack(WaitPackStrategy.any(currentPacks[0].id))
        return this
    }

    fun sortByWorkingPointsProfitDesc() : PacksPageObject{
        orderComponent.orderBy(orderComponent.orderableColumns().find { it.name == "Profit Per Working Point" }!!.copy(direction = OrderDirection.DESC))
        currentPacks = waitForNewPacks()
        return this
    }

    fun sortByWorkingPointsProfitAsc() : PacksPageObject{
        orderComponent.orderBy(orderComponent.orderableColumns().find { it.name == "Profit Per Working Point" }!!.copy(direction = OrderDirection.ASC))
        currentPacks = waitForNewPacks()
        return this
    }

    open fun userID() : Long?{
        return null
    }

    private fun WaitPackStrategy.toXpath() : By = when(val strategy = this){
        is WaitPackStrategy.LastPackWaitStrategy -> By.xpath("//tbody/tr[@Id='pack_${strategy.id}' and position() = last()]")
        is WaitPackStrategy.SpecificPositionWaitStrategy -> By.xpath("//tbody/tr[@Id='pack_${strategy.id}' and position() = ${strategy.position}]")
        is WaitPackStrategy.AnyPosition -> By.xpath("//tbody/tr[@Id='pack_${strategy.id}']")
    }

    private fun selectArcheageServer(archeageServer: ArcheageServer) {
        By.xpath("//a[text() = 'Server']").scrollInto(driver)
        By.xpath("//a[text() = 'Server']").click(driver)
        By.id("server_${archeageServer.id!!}").click(driver)
    }
    private fun waitForPackToDisappear(waitStrategy: WaitPackStrategy) {

        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(2))
            .ignoring(NoSuchElementException::class.java)
            .until(invisibilityOfElementLocated(waitStrategy.toXpath()))
    }

    private fun waitForPack(waitStrategy: WaitPackStrategy) {
        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(2))
            .ignoring(NoSuchElementException::class.java)
            .until(presenceOfElementLocated(waitStrategy.toXpath()))
    }

    private fun packs() : List<PackDTO> {
        val departureLocation = try {
            if(departureLocations.selectedValue() == "all"){
                null
            }else{
                departureLocations.selectedValue().toLong()
            }
        } catch (e: NoSuchElementException) {
            null
        }

        val destinationLocation = try {
            if(destinationLocations.selectedValue() == "all"){
                null
            }else{
                destinationLocations.selectedValue().toLong()
            }
        } catch (e: NoSuchElementException) {
            null
        }

        val categories = categoriesComponent.selectedCategories()

        val packRequest = PackRequest(Continent.valueOf(continents.selectedValue()), departureLocation, destinationLocation, userID(), categories)
        val pageable = PageRequest.of(currentPage, currentPageSize(), orderComponent.orderableColumns().toSort())
        return packService.packs(packRequest, pageable,currentArcheageServer!!).content
    }

    private fun List<OrderableColumn>.toSort(): Sort {
        val orders = flatMap { (name, direction) ->
            val mappedNames = when(name){
                "Net Profit" -> listOf("netProfit.gold","netProfit.silver","netProfit.copper")
                "Profit Per Working Point" -> listOf("workingPointsProfit.gold","workingPointsProfit.silver","workingPointsProfit.copper")
                else -> throw IllegalArgumentException("Unknown ordering column $name.")
            }
            when (direction) {
                OrderDirection.ASC -> mappedNames.map { Sort.Order.asc(it) }
                OrderDirection.DESC -> mappedNames.map { Sort.Order.desc(it) }
                OrderDirection.NONE -> emptyList()
            }
        }.toList()

        return Sort.by(orders)
    }

    private fun waitForPacks(){
        currentPacks.forEachIndexed{ index,pack ->
            waitForPack(WaitPackStrategy.at(pack.id,index+1))
        }
    }

    private fun waitForNewPacks() : List<PackDTO> {
        val newPacks = packs()
        val packToDisappear = currentPacks.find { it.id !in newPacks.map(PackDTO::id) }?.id
        val packToAppear = newPacks.find { it.id !in currentPacks.map(PackDTO::id) }?.id

        if(packToDisappear != null){
            waitForPackToDisappear(WaitPackStrategy.any(packToDisappear))
        }

        if(packToAppear != null){
            waitForPack(WaitPackStrategy.any(packToAppear))
        }

        return newPacks
    }

    private fun checkArcheageServerSelected() {
        if (currentArcheageServer == null) throw IllegalStateException("First select archeage server.")
    }

    fun paginationData(): PaginationData = paginationComponent.data()

}

private sealed class WaitPackStrategy(val id:Long){

    companion object{

        fun firstPack(packID: Long) : WaitPackStrategy = SpecificPositionWaitStrategy(packID,1)

        fun lastPack(packID: Long) : WaitPackStrategy = LastPackWaitStrategy(packID)

        fun any(packID: Long) : WaitPackStrategy = AnyPosition(packID)

        fun at(packID: Long,position: Int) : WaitPackStrategy = SpecificPositionWaitStrategy(packID,position)
    }

    class LastPackWaitStrategy(id: Long) : WaitPackStrategy(id)

    class SpecificPositionWaitStrategy(id: Long,val position: Int) : WaitPackStrategy(id)

    class AnyPosition(id: Long) : WaitPackStrategy(id)
}
