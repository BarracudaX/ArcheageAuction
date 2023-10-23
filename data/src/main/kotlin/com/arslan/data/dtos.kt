package com.arslan.data

data class PackDTO(val name: String,val creationLocation: String,val recipes: Set<RecipeDTO>,val id: Long)

data class RecipeDTO(val quantity: Int,val materials: Set<CraftingMaterialDTO>, val id: Long)

data class CraftingMaterialDTO(val quantity: Int,val material: ItemDTO)

data class ItemDTO(val name: String,val id: Long)