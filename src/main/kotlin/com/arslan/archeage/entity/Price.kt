package com.arslan.archeage.entity

import jakarta.persistence.Embeddable
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

private enum class PriceValue{ POSITIVE,NEGATIVE,ZERO}

const val MAX_COIN_VALUE = 99
const val MIN_COIN_VALUE = -99
const val COPPER_COINS_PER_SILVER_COIN = 100
const val SILVER_COINS_PER_GOLD_COIN = 100
const val COPPER_COINS_PER_GOLD_COIN = 10000

@Serializable
@Embeddable
data class Price(val gold: Int,val silver: Int, val copper: Int) : Comparable<Price>{

    init {
        require(silver in MIN_COIN_VALUE..MAX_COIN_VALUE){ "Silver property should be between [$MIN_COIN_VALUE,$MAX_COIN_VALUE]. Provided value $silver." }
        require(copper in MIN_COIN_VALUE..MAX_COIN_VALUE){ "Copper property should be between [$MIN_COIN_VALUE,$MAX_COIN_VALUE]. Provided value $copper." }

        val priceValue = if(gold != 0){
            if(gold > 0){
                PriceValue.POSITIVE
            }else{
                PriceValue.NEGATIVE
            }
        }else if(silver != 0){
            if(silver > 0){
                PriceValue.POSITIVE
            }else{
                PriceValue.NEGATIVE
            }
        }else if(copper != 0){
            if(copper > 0){
                PriceValue.POSITIVE
            }else{
                PriceValue.NEGATIVE
            }
        }else{
            PriceValue.ZERO
        }

        when(priceValue){
            PriceValue.POSITIVE -> require(gold >=0 && silver >= 0 && copper >= 0){"Partially positive price not allowed: $gold gold, $silver silver, $copper copper."}
            PriceValue.NEGATIVE -> require(gold <= 0 && silver <= 0 && copper <= 0){ "Partially negative price not allowed: $gold gold, $silver silver, $copper copper." }
            PriceValue.ZERO -> {}
        }

    }

    companion object{

        @JvmStatic
        val ZERO = Price(0,0,0)

        fun fromCopper(amount: Int) : Price {
            val gold = amount / COPPER_COINS_PER_GOLD_COIN
            val silver = (amount-gold*COPPER_COINS_PER_GOLD_COIN)/ SILVER_COINS_PER_GOLD_COIN
            val copper = (amount-gold* COPPER_COINS_PER_GOLD_COIN-silver* COPPER_COINS_PER_SILVER_COIN)

            return Price(gold,silver,copper)
        }

        fun of(gold: Int,silver: Int,copper: Int) : Price = Price(gold,silver,copper)
    }

    private fun totalCopper() : Int = copper + silver* COPPER_COINS_PER_SILVER_COIN + gold*SILVER_COINS_PER_GOLD_COIN*COPPER_COINS_PER_SILVER_COIN

    operator fun plus(price: Price) : Price = fromCopper(totalCopper() + price.totalCopper())

    operator fun minus(price: Price) : Price {
        return fromCopper(totalCopper() - price.totalCopper())
    }

    operator fun times(i: Int) : Price {
        return fromCopper(totalCopper()*i)
    }

    operator fun times(d: Double) : Price{
        return fromCopper((totalCopper()*d).toInt())
    }

    operator fun div(i: Int) : Price{
        return fromCopper(totalCopper()/i)
    }

    override fun compareTo(other: Price): Int {
        val totalCopperCost = copper + silver * COPPER_COINS_PER_SILVER_COIN + gold * SILVER_COINS_PER_GOLD_COIN * COPPER_COINS_PER_SILVER_COIN
        val otherTotalCopperCost = other.copper + other.silver * COPPER_COINS_PER_SILVER_COIN + other.gold * SILVER_COINS_PER_GOLD_COIN * COPPER_COINS_PER_SILVER_COIN

        return if(totalCopperCost == otherTotalCopperCost){
            0
        }else if(totalCopperCost > otherTotalCopperCost) {
            1
        }else{
            -1
        }
    }


}