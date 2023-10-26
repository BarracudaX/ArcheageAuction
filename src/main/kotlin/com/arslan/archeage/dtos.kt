package com.arslan.archeage

import com.arslan.archeage.entity.Price
import java.time.Instant

enum class Continent{ EAST,WEST,NORTH }

data class PackDTO(val name: String, val creationLocation: String, val destinationLocation: String, val sellPrice: Price, val recipes: List<RecipeDTO>)

data class RecipeDTO(val quantity: Int, val materials: List<CraftingMaterialDTO>, val id: Long)

data class CraftingMaterialDTO(val quantity: Int, val itemDTO: ItemDTO, val priceDTO: Price? = null)

data class ItemDTO(val name: String,val id: Long)

