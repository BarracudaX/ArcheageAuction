package com.arslan.archeage.pageobjects.component

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.FluentWait
import org.openqa.selenium.support.ui.LoadableComponent
import org.openqa.selenium.support.ui.Select
import java.time.Duration

class SelectComponent<V>(
    selectID: String,
    private val driver: WebDriver,
    private val condition: (SelectComponent<V>) -> ExpectedCondition<*>,
    private val convertToValue: (V) -> String
) : LoadableComponent<SelectComponent<V>>() {

    private val wait = FluentWait(driver).withTimeout(Duration.ofSeconds(2))
    val optionsBy = By.xpath("//select[@id='${selectID}']/option")
    private val selectBy = By.xpath("//select[@id='${selectID}']")

    fun options() : List<String> = driver.findElements(optionsBy).map { it.text }

    fun selectedValue() : String = Select(driver.findElement(selectBy)).firstSelectedOption.getAttribute("value")

    fun selectValue(value: V){
        Select(driver.findElement(selectBy)).selectByValue(convertToValue(value))
    }

    override fun load() {

    }

    public override fun isLoaded() {
        wait.until(condition(this))
    }

}