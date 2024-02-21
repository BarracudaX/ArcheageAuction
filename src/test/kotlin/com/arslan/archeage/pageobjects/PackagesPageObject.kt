package com.arslan.archeage.pageobjects

import com.arslan.archeage.entity.ArcheageServer
import io.kotest.assertions.fail
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.LoadableComponent
import java.time.Duration

// page_url = http://localhost:8080/packs_view
class PackagesPageObject(private val driver: WebDriver, private val port: Int) : AbstractUnauthenticatedPageObject<PackagesPageObject>(driver,port) {
    
    private val continents = By.id("continent")
    private val departureLocations = By.id("departure_location")
    private val destinationLocations = By.id("destination_location")
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
        val condition = or(presenceOfElementLocated(error), or(numberOfElementsToBeMoreThan(destinationLocations,1),numberOfElementsToBeMoreThan(departureLocations,1)))
        val wait = FluentWait(driver)
            .withTimeout(Duration.ofSeconds(2))
            .ignoring(NoSuchElementException::class.java)
        try{
            wait.until(condition)
        }catch (ex: TimeoutException){
            fail("Not Loaded")
        }
    }

    fun selectServer(archeageServer: ArcheageServer){
        driver.findElement(By.xpath("//a[text() = 'Server']")).click()
        driver.findElement(By.id("server_${archeageServer.id!!}")).click()
        get()
    }

    fun packs() : List<PagePack>{
        TODO()
    }

    fun error() : String? {
        return try{
            driver.findElement(error).text
        }catch (_: NoSuchElementException){
            null
        }
    }

}

data class PagePack(val name: String)