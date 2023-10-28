package com.arslan.archeage.unit

import com.arslan.archeage.entity.Price
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PriceUnitTest {

    @Test
    fun `should add two prices and return new price that is sum of the two`() {
        val priceOne = Price(10,89,76)
        val priceTwo = Price(8,25,44)
        val expectedResult = Price(19,15,20)

        priceOne + priceTwo shouldBe expectedResult
    }

    @Test
    fun `should multiply the price with specified value and return new price`() {
        val price = Price(8,68,58)
        val multiplier = 3
        val expectedResult = Price(26,5,74)

        price * multiplier shouldBe expectedResult
    }

    @Test
    fun `should return 0 price when multiplying with zero`() {
        val price = Price(5,24,95)

        price * 0 shouldBe Price(0,0,0)
    }

    @Test
    fun `should throw IAE when multiplying with negative number`() {
        shouldThrow<IllegalArgumentException> { Price(2,3,4) * -1 }
    }

    @Test
    fun `should subtract two prices and return new price`() {
        val priceOne = Price(10,23,18)
        val priceTwo = Price(6,98,24)
        val expectedResultOne = Price(3,24,94)
        val expectedResulTwo = Price(-3,-24,-94)

        priceOne - priceTwo shouldBe expectedResultOne
        priceTwo - priceOne shouldBe expectedResulTwo
    }
}