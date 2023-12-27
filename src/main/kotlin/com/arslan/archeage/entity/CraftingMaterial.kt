package com.arslan.archeage.entity

import com.arslan.archeage.entity.item.Item
import jakarta.persistence.Embeddable
import jakarta.persistence.ManyToOne

@Embeddable
class CraftingMaterial(
    var quantity: Int,

    @ManyToOne
    var item: Item,
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CraftingMaterial

        return item == other.item
    }

    override fun hashCode(): Int {
        return item.hashCode()
    }
}