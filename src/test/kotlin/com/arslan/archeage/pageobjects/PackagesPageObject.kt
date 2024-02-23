package com.arslan.archeage.pageobjects

import capitalized
import com.arslan.archeage.Continent
import com.arslan.archeage.NoOpCondition
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.pageobjects.component.SelectComponent
import io.kotest.assertions.fail
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.Select
import java.time.Duration

// page_url = http://localhost:8080/packs_view
class PackagesPageObject(private val driver: WebDriver, private val port: Int) : AbstractUnauthenticatedPageObject<PackagesPageObject>(driver,port) {

    private val continents = SelectComponent<Continent>(
        "continent",
        driver,{ select -> numberOfElementsToBe(select.optionsBy, Continent.entries.size)},
        { continent -> continent.name }
    )
    private val departureLocations = SelectComponent<Location>(
        "departure_location",
        driver,
        { NoOpCondition() },
        { location -> location.id.toString() }
    )
    private val destinationLocations = SelectComponent<Location>(
        "destination_location",
        driver,
        { NoOpCondition() },
        {location -> location.id.toString()}
    )

    private val categoriesBtn = By.id("categories_btn")
    private val categoriesOffCanvas = By.id("categories-offcanvas")
    private val previousBtn = By.id("previous_btn")
    private val nextBtn = By.id("next_btn")
    private val packs = By.className("pack")
    private val error = By.cssSelector("div.alert.alert-danger.alert-dismissible")

    override fun load() {
        driver.manage().window().maximize()
        driver.get("http://localhost:${port}/packs_view")
    }

    override fun isSubclassLoaded() {
        val condition = or(presenceOfElementLocated(error), numberOfElementsToBeMoreThan(departureLocations.optionsBy, 0))
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

    fun selectServer(archeageServer: ArcheageServer) : PackagesPageObject{
        driver.findElement(By.xpath("//a[text() = 'Server']")).click()
        driver.findElement(By.id("server_${archeageServer.id!!}")).click()
        return get()
    }

    fun continents() : List<String> = continents.options()

    fun selectedContinent() : Continent = Continent.valueOf(continents.selectedValue())

    fun departureLocations(consumer: List<String>.() -> Unit) : PackagesPageObject {
        departureLocations.options().apply(consumer)
        return this
    }

    fun destinationLocations(consumer: List<String>.() -> Unit) : PackagesPageObject {
        destinationLocations.options().apply(consumer)

        return this
    }

    fun error(consumer: String?.() -> Unit) : PackagesPageObject {
        try{
            driver.findElement(error).text
        }catch (_: NoSuchElementException){
            null
        }.apply(consumer)

        return this
    }

    /**
     * Selects provided continent and waits for the provided location to be loaded before returning.
     */
    fun selectContinent(continent: Continent,location: Location) : PackagesPageObject {
        continents.selectValue(continent)
        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(5))
            .until(numberOfElementsToBeMoreThan(By.xpath("//option[text()='${location.name.lowercase().capitalized()}']"),0))

        return this
    }

    fun selectDepartureLocation(location: Location) : PackagesPageObject{
        departureLocations.selectValue(location)
        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(5))
            .until { driver ->
                val departureLocation = (driver as JavascriptExecutor).executeScript("""
                    return selectedDepartureLocation
                """.trimIndent()) as Long?

                departureLocation == location.id
            }

        return this
    }

    fun selectDestinationLocation(location: Location) : PackagesPageObject{
        destinationLocations.selectValue(location)

        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(5))
            .until { driver ->
                val destinationLocation = (driver as JavascriptExecutor).executeScript("""
                    return selectedDestinationLocation
                """.trimIndent()) as Long?

                destinationLocation == location.id
            }

        return this
    }

}