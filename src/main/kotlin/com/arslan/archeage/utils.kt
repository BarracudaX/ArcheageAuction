package com.arslan.archeage

import com.arslan.archeage.entity.*

fun Recipe.toDTO(prices: Map<String, ItemPrice>): RecipeDTO = RecipeDTO(producedQuantity,materials.map { material ->  material.toDTO(prices) },id!!)

fun CraftingMaterial.toDTO(prices: Map<String, ItemPrice>) : CraftingMaterialDTO = CraftingMaterialDTO(quantity,item.toDTO(),prices[item.name]?.price)

fun Item.toDTO() : ItemDTO = ItemDTO(name,id!!)

fun List<Pack>.toDTO(prices: Map<String,ItemPrice>) : List<PackDTO> {
    return flatMap { pack ->
        pack.prices
            .filterIsInstance<PackPrice>()
            .flatMap { packPrice ->
                pack.recipes.map { recipe ->
                    PackDTO(pack.name, pack.creationLocation.name, packPrice.sellLocation.name, packPrice.price, RecipeDTO(recipe.producedQuantity, recipe.materials.map { material -> material.toDTO(prices) }, recipe.id!!))
                }
            }
    }
}