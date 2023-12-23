package com.arslan.archeage.entity

import jakarta.persistence.Embeddable
import kotlinx.serialization.Serializable

@Serializable
@Embeddable
data class Price(val gold: Int,val silver: Int, val copper: Int) : Comparable<Price>{

    init {
        if(gold > 99) throw IllegalArgumentException("Gold property should be between [0,99]. Provided value $gold.")
        if(silver > 99) throw IllegalArgumentException("Silver property should be between [0,99]. Provided value $silver.")
        if(copper > 99) throw IllegalArgumentException("Copper property should be between [0,99]. Provided value $copper.")
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

    operator fun plus(price: Price) : Price = Price(gold + price.gold + (silver + price.silver)/100,(silver + price.silver) % 100 + (copper+price.copper)/100,(copper + price.copper)%100 )

    operator fun minus(price: Price) : Price {
        return fromCopper(totalCopper() - price.totalCopper())
    }

    operator fun times(i: Int) : Price {
        if(i < 0)
            throw IllegalArgumentException("Multiplier must be zero or positive number.")

        return Price(gold*i+silver*i/100,(silver*i+copper*i/100)%100,copper*i%100)
    }

    override fun compareTo(other: Price): Int {
        val totalCopperCost = copper + silver * 100 + gold * 100 * 100
        val otherTotalCopperCost = other.copper + other.silver * 100 + other.gold * 100 * 100

        if(totalCopperCost == otherTotalCopperCost) return 0

        if(totalCopperCost > otherTotalCopperCost) return 1

        return -1
    }


}