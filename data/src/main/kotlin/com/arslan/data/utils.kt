package com.arslan.data

import com.arslan.data.entity.CraftingMaterial
import com.arslan.data.entity.Item
import com.arslan.data.entity.Pack
import com.arslan.data.entity.Recipe

fun Pack.toDTO() : PackDTO = PackDTO(name,creationLocation.name,recipes.map(Recipe::toDTO).toSet(),id!!)

fun Recipe.toDTO() : RecipeDTO = RecipeDTO(producedQuantity,materials.map(CraftingMaterial::toDTO).toSet(),id!!)

fun CraftingMaterial.toDTO() : CraftingMaterialDTO = CraftingMaterialDTO(quantity,item.toDTO())

fun Item.toDTO() : ItemDTO = ItemDTO(name,id!!)