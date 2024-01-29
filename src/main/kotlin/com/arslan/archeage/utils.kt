package com.arslan.archeage

import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.pack.Pack

fun CraftingMaterial.toDTO(prices: Map<Long, UserPrice>): CraftingMaterialDTO =
    CraftingMaterialDTO(quantity, item.toDTO(prices[item.id!!]?.price),prices[item.id!!]?.price?.times(quantity))

fun Item.toDTO(price: Price?): ItemDTO = ItemDTO(name, id!!,price)

fun Pack.toDTO(prices: Map<Long, UserPrice>,percentage: Double) : PackDTO {
    val cost = materials().fold(Price(0,0,0)){ cost,material -> cost + (prices[material.item.id!!]?.price ?: Price(0,0,0))*material.quantity }

    val profit = price.price*percentage - cost

    return PackDTO(name,creationLocation.name,price.sellLocation.name,price.price,producedQuantity,materials().map { it.toDTO(prices) } ,id!!,cost,profit)
}

fun List<Pack>.toDTO(prices: Map<Long, UserPrice>,percentages: Map<Long,Double>): List<PackDTO> = map { pack -> pack.toDTO(prices,percentages[pack.id!!]!!) }