package com.arslan.archeage

import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.Item
import com.arslan.archeage.entity.ItemPrice
import com.arslan.archeage.entity.Recipe

fun Recipe.toDTO(prices: Map<String, ItemPrice>): RecipeDTO = RecipeDTO(producedQuantity,materials.map { material ->  material.toDTO(prices) },id!!)

fun CraftingMaterial.toDTO(prices: Map<String, ItemPrice>) : CraftingMaterialDTO = CraftingMaterialDTO(quantity,item.toDTO(),prices[item.name]?.price)

fun Item.toDTO() : ItemDTO = ItemDTO(name,id!!)