package com.arslan.archeage.selenium

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder

class CommonSeleniumPage : SeleniumTest() {

    @Test
    fun `index title page`() {
        webDriver.get(url)
        webDriver.title shouldBe messageSource.getMessage("page.title", emptyArray(),LocaleContextHolder.getLocale())
    }



}