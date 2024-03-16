package com.arslan.archeage

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

fun String.capitalized() : String = replaceFirstChar { if(it.isLowerCase()){ it.titlecase() }else{ it.toString() } }

fun By.scrollInto(driver: WebDriver){
    (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(this))
}

fun WebElement.scrollInto(driver: WebDriver){
    (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView(true);", this)
}

fun By.click(driver: WebDriver){
    (driver as JavascriptExecutor).executeScript("arguments[0].click();", driver.findElement(this));
}

fun WebElement.click(driver: WebDriver){
    (driver as JavascriptExecutor).executeScript("arguments[0].click();", this);
}

fun Collection<PackDTO>.sortByWorkingPointsProfitDesc() = sortedByDescending(PackDTO::workingPointsProfit)
fun Collection<PackDTO>.sortByWorkingPointsProfitAsc() = sortedBy(PackDTO::workingPointsProfit)

fun <T> List<T>.toIndex(upperBound: Int) : Int = if(upperBound <= size){
    upperBound
}else{
    size
}