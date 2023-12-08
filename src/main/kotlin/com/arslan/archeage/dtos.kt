package com.arslan.archeage

import com.arslan.archeage.entity.Price
import org.springframework.data.domain.Page

enum class Continent{ EAST,WEST,NORTH }

data class PackDTO(val name: String, val creationLocation: String, val destinationLocation: String, val sellPrice: Price, val recipeDTO: RecipeDTO){

    fun profit() : Price = sellPrice - recipeDTO.cost()

}
data class RecipeDTO(val quantity: Int, val materials: List<CraftingMaterialDTO>, val id: Long) {

    private var cost: Price? = null

    fun cost() : Price{
        if(cost == null)
            cost =  materials.fold(Price(0,0,0)){ cost, material -> cost + (material.price ?: Price(0,0,0)) * material.quantity }

        return cost!!
    }

}

data class CraftingMaterialDTO(val quantity: Int, val itemDTO: ItemDTO, val price: Price? = null)

class ItemDTO(val name: String,val id: Long){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemDTO

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

data class UserPrices(val items: Page<ItemDTO>,val prices: Map<Long,Price>,val hasNext: Boolean,val hasPrevious: Boolean)
