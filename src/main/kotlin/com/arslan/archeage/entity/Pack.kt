package com.arslan.archeage.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "PACKS")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    name: String,

    description: String
) : Item(name, description, creationLocation.region){

    override fun addPrice(price: ItemPrice) {
        if(price !is PackPrice) throw IllegalArgumentException("Only pack prices are allowed for pack entities.")
        super.addPrice(price)
    }
}