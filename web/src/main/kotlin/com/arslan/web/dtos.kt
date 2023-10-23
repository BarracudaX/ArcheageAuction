package com.arslan.web

import java.time.Instant

enum class Continent{ EAST,WEST,NORTH }

data class LocationDTO(val continent: Continent,val name: String)

data class PriceDTO(val gold: Int, val silver: Int,val copper: Int)

data class DataPackDTO(val name: String,val creationLocation: String,val recipes: Set<DataRecipeDTO>,val id: Long)

data class DataRecipeDTO(val quantity: Int,val materials: Set<DataCraftingMaterialDTO>, val id: Long)

data class DataCraftingMaterialDTO(val quantity: Int,val material: ItemDTO)

data class ItemPriceDTO(val itemName: String,val price: PriceDTO,val timestamp: Instant,val id: Long,val destinationLocation: String? = null)

data class PackDTO(val name: String, val creationLocation: String, val destinationLocation: String, val sellPrice: PriceDTO, val recipes: Set<RecipeDTO>)

data class RecipeDTO(val quantity: Int,val materials: Set<CraftingMaterialDTO>, val id: Long)

data class CraftingMaterialDTO(val quantity: Int,val itemDTO: ItemDTO,val priceDTO: PriceDTO? = null)

data class ItemDTO(val name: String,val id: Long)