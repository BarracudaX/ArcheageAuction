package com.arslan.archeage

import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice

fun CraftingMaterial.toDTO(prices: Map<Long, UserPrice>): CraftingMaterialDTO =
    CraftingMaterialDTO(quantity, item.toDTO(prices[item.id!!]?.price))

fun Item.toDTO(price: Price?): ItemDTO = ItemDTO(name, id!!,price)

fun List<Pack>.toDTO(prices: Map<Long, UserPrice>): List<PackDTO> {
    return flatMap { pack ->
        pack.prices()
            .flatMap { packPrice ->
                pack.recipes()
                    .filter { recipe ->
                        prices.keys.containsAll(recipe.materials().map(CraftingMaterial::item).filterIsInstance<PurchasableItem>().map(PurchasableItem::id))
                    }.map { recipe ->
                        PackDTO(pack.name, pack.creationLocation.name, packPrice.sellLocation.name, packPrice.price, RecipeDTO(recipe.producedQuantity, recipe.materials().map { material -> material.toDTO(prices) }, recipe.id!!))
                    }
            }
    }
}

fun List<PackDTO>.materialsWithPrice(): List<CraftingMaterialDTO> = flatMap { pack -> pack.recipeDTO.materials }.filter { material -> material.itemDTO.price != null }