package com.arslan.archeage

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition

class NoOpCondition : ExpectedCondition<Unit> {
    override fun apply(input: WebDriver?) {

    }

}