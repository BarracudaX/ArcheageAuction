package com.arslan.archeage.pageobjects.component

import click
import com.arslan.archeage.pageobjects.component.OrderDirection.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import scrollInto
import java.time.Duration

class TableOrderComponent(private val driver: WebDriver, private val tableID: String) {

    private val headerColumns = By.xpath("//*[@id='${tableID}']/thead/tr[1]/th")

    fun orderableColumns() : List<OrderableColumn>{
        val orderBy = driver.findElement(headerColumns)
        orderBy.scrollInto(driver)
        FluentWait(driver).withTimeout(Duration.ofSeconds(2)).until(ExpectedConditions.visibilityOfElementLocated(headerColumns))
        return driver.findElements(headerColumns).filter { element ->
            val classAttribute = element.getAttribute("class")
            !classAttribute.contains("dt-orderable-none") && classAttribute.contains("dt-orderable-asc") && classAttribute.contains("dt-orderable-desc")
        }.map { orderableColumn ->
            val classAttribute = orderableColumn.getAttribute("class")
            val direction = if(classAttribute.contains("dt-ordering-asc")){
                ASC
            }else if(classAttribute.contains("dt-ordering-desc")){
                DESC
            }else{
                NONE
            }
            OrderableColumn(orderableColumn.text,direction)
        }
    }

    fun orderBy(column: OrderableColumn) {
        val orderBy = driver.findElement(By.xpath("//*[@id='${tableID}']/thead/tr[1]/th[span = '${column.name}']"))
        orderBy.scrollInto(driver)
        when(column.direction){
            ASC -> orderAsc(orderBy)
            DESC -> orderDesc(orderBy)
            NONE -> { }
        }

    }

    private fun orderAsc(element: WebElement){
        if(element.getAttribute("class").contains("dt-ordering-asc")){
            return
        }
        element.click()
        orderAsc(element)
    }

    private fun orderDesc(element: WebElement){
        if(element.getAttribute("class").contains("dt-ordering-desc")){
            return
        }
        element.click(driver)
        orderDesc(element)
    }

}

enum class OrderDirection{
    ASC,DESC,NONE
}

data class OrderableColumn(val name: String,val direction: OrderDirection)