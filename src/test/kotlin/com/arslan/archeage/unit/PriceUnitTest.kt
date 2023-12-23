package com.arslan.archeage.unit

import com.arslan.archeage.entity.Price
import io.kotest.assertions.throwables.shouldNotThrow
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
    fun `should add two prices and return new price that is sum of the two (2)`() {
        val priceOne = Price(-20,-80,-90)
        val priceTwo = Price(10,90,80)

        priceOne + priceTwo shouldBe Price(-9,-90,-10)
    }

    @Test
    fun `should multiply the price with specified value and return new price`() {
        val price = Price(8,68,58)
        val multiplier = 3
        val expectedResult = Price(26,5,74)

        price * multiplier shouldBe expectedResult
    }

    @Test
    fun `should multiply the price with specified value and return new price (2)`() {
        val price = Price(-8,-68,-58)
        val multiplier = 3
        val expectedResult = Price(-26,-5,-74)

        price * multiplier shouldBe expectedResult
    }

    @Test
    fun `should multiply the price with specified value and return new price (3)`() {
        val price = Price(8,68,58)
        val multiplier = -3
        val expectedResult = Price(-26,-5,-74)

        price * multiplier shouldBe expectedResult
    }

    @Test
    fun `should multiply the price with specified value and return new price (4)`() {
        val price = Price(-8,-68,-58)
        val multiplier = -3
        val expectedResult = Price(26,5,74)

        price * multiplier shouldBe expectedResult
    }

    @Test
    fun `should return 0 price when multiplying with zero`() {
        val price = Price(5,24,95)

        price * 0 shouldBe Price(0,0,0)
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


    @Test
    fun `should throw IAE when silver is greater than 99`() {
        shouldThrow<IllegalArgumentException> { Price(0,100,0) }
        shouldNotThrow<IllegalArgumentException> { Price(0,99,0) }
    }

    @Test
    fun `should throw IAE when silver is less than -99`() {
        shouldThrow<IllegalArgumentException> { Price(0,-100,0) }
        shouldNotThrow<IllegalArgumentException> { Price(0,-99,0) }
    }

    @Test
    fun `should throw IAE when copper is greater than 99`() {
        shouldThrow<IllegalArgumentException> { Price(0,0,100) }
        shouldNotThrow<IllegalArgumentException> { Price(0,0,99) }
    }

    @Test
    fun `should throw IAE when copper is less than -99`() {
        shouldThrow<IllegalArgumentException> { Price(0,0,-100) }
        shouldNotThrow<IllegalArgumentException> { Price(0,0,-99) }
    }

    @Test
    fun `should throw IllegalArgumentException when price is partially positive`() {
        shouldThrow<IllegalArgumentException> { Price(1,-1,0) }
        shouldThrow<IllegalArgumentException> { Price(1,0,-1) }
        shouldThrow<IllegalArgumentException> { Price(1,-1,-1) }
        shouldThrow<IllegalArgumentException> { Price(0,1,-1) }

        shouldNotThrow<IllegalArgumentException> { Price(0,0,1) }
        shouldNotThrow<IllegalArgumentException> { Price(0,1,0) }
        shouldNotThrow<IllegalArgumentException> { Price(0,1,1) }
        shouldNotThrow<IllegalArgumentException> { Price(1,0,0) }
        shouldNotThrow<IllegalArgumentException> { Price(1,0,1) }
        shouldNotThrow<IllegalArgumentException> { Price(1,1,0) }
        shouldNotThrow<IllegalArgumentException> { Price(1,1,1) }
    }

    @Test
    fun `should throw IllegalArgumentException when price is partially negative`() {
        shouldThrow<IllegalArgumentException> { Price(-1,1,0) }
        shouldThrow<IllegalArgumentException> { Price(-1,0,1) }
        shouldThrow<IllegalArgumentException> { Price(-1,1,1) }
        shouldThrow<IllegalArgumentException> { Price(0,-1,1) }

        shouldNotThrow<IllegalArgumentException> { Price(0,0,-1) }
        shouldNotThrow<IllegalArgumentException> { Price(0,-1,0) }
        shouldNotThrow<IllegalArgumentException> { Price(0,-1,-1) }
        shouldNotThrow<IllegalArgumentException> { Price(-1,0,0) }
        shouldNotThrow<IllegalArgumentException> { Price(-1,0,-1) }
        shouldNotThrow<IllegalArgumentException> { Price(-1,-1,0) }
        shouldNotThrow<IllegalArgumentException> { Price(-1,-1,-1) }
    }

    @Test
    fun `should not throw IAE when price is zero`() {
        shouldNotThrow<IllegalArgumentException> { Price(0,0,0) }
    }
}