package com.arslan.archeage.pageobjects.component

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class PaginationComponent(private val driver: WebDriver) {

    private val firstBy = By.xpath("//ul[@class='pagination']/li[1]")
    private val previousBy = By.xpath("//ul[@class='pagination']/li[2]")
    private val lastBy = By.xpath("//ul[@class='pagination']/li[last()]")
    private val nextBy = By.xpath("//ul[@class='pagination']/li[last() - 1]")
    private val pageNumsBy = By.xpath("//ul[@class='pagination']/li[position() > 2 and position() < last() - 1]")

    fun data(): PaginationData {
        val firstBtn = driver.findElement(firstBy).toPaginationButton()
        val previousBtn = driver.findElement(previousBy).toPaginationButton()
        val paginationNums =
            driver.findElements(pageNumsBy).filter { it.text.toIntOrNull() != null }.map { it.toPaginationButton() }
        val nextBtn = driver.findElement(nextBy).toPaginationButton()
        val lastBtn = driver.findElement(lastBy).toPaginationButton()

        return PaginationData(firstBtn, previousBtn, paginationNums, nextBtn, lastBtn)
    }


    fun selectPage(pageNum: Int) {
        driver.findElement(By.xpath("//ul[@class='pagination']/li/a[text() = '$pageNum']")).click()
    }

    fun nextPage() {
        driver.findElement(nextBy).click()
    }

    fun lastPage() {
        driver.findElement(lastBy).click()
    }

    fun previousPage() {
        driver.findElement(previousBy).click()
    }

    fun firstPage() {
        driver.findElement(firstBy).click()
    }

    private fun WebElement.toPaginationButton(): PaginationButton {
        val elementState = if (getAttribute("class").contains("disabled")) {
            ElementState.DISABLED
        } else {
            ElementState.ENABLED
        }

        return PaginationButton(text, elementState, getAttribute("class").contains("active"))
    }
}

enum class ElementState{ ENABLED,DISABLED }

data class PaginationButton(val content: String,val elementState: ElementState,val isActive: Boolean)

data class PaginationData(val firstBtn: PaginationButton,val previousBtn: PaginationButton,val paginationNums: List<PaginationButton>,val nextBtn: PaginationButton,val lastBtn: PaginationButton)