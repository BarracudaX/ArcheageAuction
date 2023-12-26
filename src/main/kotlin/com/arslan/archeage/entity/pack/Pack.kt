package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import jakarta.persistence.*

@Entity
@Table(name = "packs")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    var price: PackPrice,

    var producedQuantity: Int,

    name: String,

    description: String
) : Item(name,description,creationLocation.archeageServer){


    @CollectionTable(name = "pack_materials")
    @ElementCollection
    private val materials: MutableSet<CraftingMaterial> = mutableSetOf()

    fun materials() : Set<CraftingMaterial> = materials

    fun addMaterial(material: CraftingMaterial){
        if(material.item.archeageServer != archeageServer) throw IllegalArgumentException("")
        materials.add(material)
    }

    fun profit(prices: Map<Long,Price>) : Price{

        val cost = materials
            .filter { it.item is PurchasableItem }
            .map { material -> prices[material.item.id]!! * material.quantity }
            .fold(Price(0,0,0)){ price,next -> price + next }

        return price.price - cost
    }
}