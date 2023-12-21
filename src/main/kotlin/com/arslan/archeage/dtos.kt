package com.arslan.archeage

import com.arslan.archeage.entity.Price
import kotlinx.serialization.Serializable
import org.springframework.data.domain.Page

enum class Continent{ EAST,WEST,NORTH }

@Serializable
data class PackDTO(val name: String, val creationLocation: String, val destinationLocation: String, val sellPrice: Price, val recipeDTO: RecipeDTO){

    val profit = sellPrice - recipeDTO.cost()


}

@Serializable
data class RecipeDTO(val quantity: Int, val materials: List<CraftingMaterialDTO>, val id: Long) {

    private var cost: Price? = null

    fun cost() : Price{
        if(cost == null)
            cost =  materials.fold(Price(0,0,0)){ cost, material -> cost + (material.itemDTO.price ?: Price(0,0,0)) * material.quantity }

        return cost!!
    }

}

@Serializable
data class CraftingMaterialDTO(val quantity: Int, val itemDTO: ItemDTO)

@Serializable
class ItemDTO(val name: String,val id: Long,val price:Price? = null){

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

@Serializable
data class UserPrices(val items: Page<ItemDTO>,val hasNext: Boolean,val hasPrevious: Boolean)

@Serializable
data class Packs(val packs: List<PackDTO>,val hasNext: Boolean,val hasPrevious: Boolean)
