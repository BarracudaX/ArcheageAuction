package com.arslan.archeage.pageobjects.component

import com.arslan.archeage.entity.Category
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.FluentWait
import java.time.Duration

class CategoriesComponent(private val driver: WebDriver) {

    private val categoriesButton = By.id("categories_btn")

    private val closeCanvasButton = By.xpath("//*[@id='categories-offcanvas']/div/button[@class='btn-close']")

    fun selectCategory(category: Category){
        driver.findElement(categoriesButton).click()

        val parents = mutableListOf<Pair<Category,Category>>()
        var currentCategory = category

        while(currentCategory.parent != null){
            parents.add(currentCategory.parent!! to currentCategory)
            currentCategory = currentCategory.parent!!
        }

        for((categoryThatNeedsExpansion,child) in parents.reversed()){
            expand(categoryThatNeedsExpansion,child)
        }

        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(2))
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("category${category.id}")))

        driver.findElement(By.id("category${category.id}")).click()

        for((categoryThatNeedsShrinking,_) in parents){
            shrink(categoryThatNeedsShrinking)
        }

        driver.findElement(closeCanvasButton).click()
    }

    private fun expand(parent: Category, child: Category){
        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(2))
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("category${parent.id}")))
        driver.findElement(By.xpath("//*[@id='category${parent.id}']/preceding-sibling::button")).click()
        FluentWait(driver)
            .withTimeout(Duration.ofSeconds(2))
            .until(ExpectedConditions.visibilityOfElementLocated(By.id("category${child.id}")))
    }

    private fun shrink(category: Category){
        driver.findElement(By.xpath("//*[@id='category${category.id}']/preceding-sibling::button")).click()
    }

}