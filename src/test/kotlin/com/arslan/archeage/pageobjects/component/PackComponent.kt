package com.arslan.archeage.pageobjects.component

import com.arslan.archeage.CraftingMaterialDTO
import com.arslan.archeage.ItemDTO
import com.arslan.archeage.NoOpCondition
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.Price
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.LoadableComponent
import java.time.Duration

class PackComponent(private val driver: WebDriver,private val id: Long) : LoadableComponent<PackComponent>(){

    private val wait = FluentWait(driver)
        .withTimeout(Duration.ofSeconds(2))
    private val condition = ExpectedConditions.visibilityOfElementLocated(By.id("pack_${id}"))
    private val expandBtnBy = By.cssSelector("#pack_${id} td[class='dt-control']")
    private val packageNameBy = By.xpath("//*[@id='pack_${id}']/td[2]")
    private val createLocationBy = By.xpath("//*[@id='pack_${id}']/td[3]")
    private val destinationLocationBy = By.xpath("//*[@id='pack_${id}']/td[4]")
    private val sellPriceBy = By.xpath("//*[@id='pack_${id}']/td[5]")
    private val profitBy = By.xpath("//*[@id='pack_${id}']/td[6]")
    private val percentageSelectBy = SelectComponent(By.xpath("//*[@id='pack_${id}']/td/select"),By.xpath("//*[@id='pack_${id}']/td/select/option"),driver,{ NoOpCondition() },Int::toString)
    private val producedQuantityBy = By.xpath("//*[@id='recipe_details_${id}']/tbody/tr/th[1]")
    private val recipeCostBy = By.xpath("//*[@id='recipe_details_${id}']/tbody/tr/th[2]")
    private val materialsBy = By.xpath("//*[@id='material_details_${id}']/tbody/tr")
    private val materialNameBy = By.xpath(".//th[1]")
    private val materialQuantityBy = By.xpath(".//th[2]")
    private val materialUnitPriceBy = By.xpath(".//th[3]")
    private val materialTotalPriceBy = By.xpath(".//th[4]")
    private var isExpanded = false

    override fun load() {
        
    }

    public override fun isLoaded() {
        wait.until(condition)
    }

    private fun expand(){
        if (!isExpanded) {
            driver.findElement(expandBtnBy).click()
            wait.until(ExpectedConditions.visibilityOfElementLocated(producedQuantityBy))
            isExpanded = true
        }
    }

    fun toPackDTO(): PackDTO {
        expand()

        val name = driver.findElement(packageNameBy).text
        val createLocation = driver.findElement(createLocationBy).text
        val destinationLocation = driver.findElement(destinationLocationBy).text
        val sellPrice = driver.findElement(sellPriceBy).text.toPrice()!!
        val quantity = driver.findElement(producedQuantityBy).text.toInt()
        val recipeCost = driver.findElement(recipeCostBy).text.toPrice()!!
        val percentage = percentageSelectBy.selectedValue().toInt()
        val profit = driver.findElement(profitBy).text.toPrice()!!
        val workingPointsProfit = Price.of(0,0,0)
        val materials = driver
            .findElements(materialsBy)
            .map { material ->
                val itemID = material.getAttribute("data-item-id").toLong()
                val materialQuantity = material.findElement(materialQuantityBy).text.toInt()
                val materialName = material.findElement(materialNameBy).text
                val materialPrice = material.findElement(materialUnitPriceBy).text.toPrice()
                val totalPrice = material.findElement(materialTotalPriceBy).text.toPrice()
                CraftingMaterialDTO(materialQuantity,ItemDTO(name,itemID,materialPrice),totalPrice)
            }

        return PackDTO(name,createLocation,destinationLocation,sellPrice,quantity,materials,id,recipeCost,percentage,profit,workingPointsProfit,false)
    }

    fun changePercentage(newPercentage: Int){
        percentageSelectBy.selectValue(newPercentage)
    }

    private fun String.toPrice() : Price? {
        val coins = replace("Gold", "")
            .replace("Silver", "")
            .replace("Copper", "")
            .split(" ")
            .mapNotNull(String::toIntOrNull)

        if(coins.size != 3)
            return null

        return Price(coins[0],coins[1],coins[2])
    }

}