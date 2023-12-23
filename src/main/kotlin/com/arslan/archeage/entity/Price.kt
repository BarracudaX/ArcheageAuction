package com.arslan.archeage.entity

import jakarta.persistence.Embeddable
import kotlinx.serialization.Serializable

private enum class PriceValue{ POSITIVE,NEGATIVE,ZERO}

@Serializable
@Embeddable
data class Price(val gold: Int,val silver: Int, val copper: Int) : Comparable<Price>{

    init {
        if(silver < -99 || silver > 99) throw IllegalArgumentException("Silver property should be between [-99,99]. Provided value $silver.")
        if(copper < -99 || copper > 99) throw IllegalArgumentException("Copper property should be between [-99,99]. Provided value $copper.")

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
            PriceValue.POSITIVE -> if(gold < 0 || silver < 0 || copper < 0) throw IllegalArgumentException("Partially positive price not allowed: $gold gold, $silver silver, $copper copper.")
            PriceValue.NEGATIVE -> if(gold > 0 || silver > 0 || copper > 0) throw IllegalArgumentException("Partially negative price not allowed: $gold gold, $silver silver, $copper copper.")
            PriceValue.ZERO -> {}
        }

    }

    companion object{

        fun fromCopper(amount: Int) : Price {
            val gold = amount / 10000
            val silver = (amount-gold*10000)/100
            val copper = (amount-gold*10000-silver*100)

            return Price(gold,silver,copper)
        }
    }

    private fun totalCopper() : Int = copper + silver*100 + gold*100*100

    operator fun plus(price: Price) : Price = fromCopper(totalCopper() + price.totalCopper())

    operator fun minus(price: Price) : Price {
        return fromCopper(totalCopper() - price.totalCopper())
    }

    operator fun times(i: Int) : Price {
        return fromCopper(totalCopper()*i)
    }

    override fun compareTo(other: Price): Int {
        val totalCopperCost = copper + silver * 100 + gold * 100 * 100
        val otherTotalCopperCost = other.copper + other.silver * 100 + other.gold * 100 * 100

        if(totalCopperCost == otherTotalCopperCost) return 0

        if(totalCopperCost > otherTotalCopperCost) return 1

        return -1
    }


}