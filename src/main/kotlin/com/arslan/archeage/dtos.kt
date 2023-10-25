package com.arslan.archeage

import java.time.Instant

enum class Continent{ EAST,WEST,NORTH }

data class PriceDTO(val gold: Int, val silver: Int,val copper: Int)

data class PackDTO(val name: String, val creationLocation: String, val destinationLocation: String, val sellPrice: PriceDTO, val recipes: Set<RecipeDTO>)

data class RecipeDTO(val quantity: Int, val materials: Set<CraftingMaterialDTO>, val id: Long)

data class CraftingMaterialDTO(val quantity: Int, val itemDTO: ItemDTO, val priceDTO: PriceDTO? = null)

data class ItemDTO(val name: String,val id: Long)

