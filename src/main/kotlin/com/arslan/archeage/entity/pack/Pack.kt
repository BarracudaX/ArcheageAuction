package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.Category
import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize

@Entity
@Table(name = "packs")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    var price: PackPrice,

    var producedQuantity: Int,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category,

    @Column(name = "working_points")
    var workingPoints: Int,

    name: String,

    description: String
) : Item(name,description,creationLocation.archeageServer){


    @BatchSize(size = 20)
    @CollectionTable(name = "pack_materials")
    @ElementCollection
    private val materials: MutableSet<CraftingMaterial> = mutableSetOf()

    fun materials() : Set<CraftingMaterial> = materials

    fun addMaterial(material: CraftingMaterial){
        require(material.item.archeageServer == archeageServer){ "" }
        materials.add(material)
    }

    fun profit(prices: Map<Long,Price>,sellPercentage: Double) : Price{
        val cost = materials
            .filter { it.item is PurchasableItem }
            .map { material -> prices[material.item.id]!! * material.quantity }
            .fold(Price(0,0,0)){ price,next -> price + next }

        return price.price*sellPercentage - cost
    }

    fun requiredQuantity(item: Item) : Int = materials.find { material -> material.item.id == item.id }?.quantity ?: throw NoSuchElementException("")
}