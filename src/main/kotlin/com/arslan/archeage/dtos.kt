package com.arslan.archeage

import com.arslan.archeage.entity.Category
import com.arslan.archeage.entity.Price
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import kotlinx.serialization.Serializable

enum class Continent{ EAST,WEST,NORTH }
@Serializable
data class PackDTO(
    val name: String,

    val creationLocation: String,

    val destinationLocation: String,

    val sellPrice: Price,

    val producedQuantity: Int,

    val materials: List<CraftingMaterialDTO>,

    val id : Long,

    val cost: Price,

    val percentage: Int,

    val profit: Price
)

@Serializable
data class CraftingMaterialDTO(val quantity: Int, val itemDTO: ItemDTO,val total: Price?)

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
data class UserPrices(val items: List<ItemDTO>,val hasNext: Boolean,val hasPrevious: Boolean)

@Serializable
data class Packs(val packs: List<PackDTO>,val hasNext: Boolean,val hasPrevious: Boolean,val isUserData: Boolean)

@Serializable
data class LocationDTO(val name: String,val id: Long)

@Serializable
data class Locations(val continentLocations: List<LocationDTO>,val continentFactories: List<LocationDTO>)

@Serializable
data class UserPriceDTO(val userID: Long? = null,val itemID: Long,val price: Price)

data class PackRequest(val continent: Continent,val departureLocation: Long?  = null,val destinationLocation: Long?  = null,val userID: Long? = null)

@Serializable
data class PackPercentageUpdate(
    val packID: Long,

    @field:DecimalMax("130")
    @field:DecimalMin("80")
    val percentage: Int,

    @field:NotNull(message = "{PackPercentageUpdate.userID.NotNull.message}")
    val userID: Long? = null
)

@Serializable
data class CategoryDTO(val id: Long,val name: String,val subcategories: MutableList<CategoryDTO> = mutableListOf()){

    fun addSubcategory(category: Category){
        if(category.parent == null) throw IllegalArgumentException("Cannot add top category as a subcategory.")

        if(id == category.parent!!.id!!){
            with(category){ subcategories.add(CategoryDTO(id!!,name)) }
        }else{
            subcategories.forEach { subCategory -> subCategory.addSubcategory(category) }
        }
    }

}