package com.arslan.archeage.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId

@Entity
class CraftingMaterial(
    var quantity: Int,

    @Id
    @ManyToOne
    @MapsId("itemID")
    var item: Item,

    @Id
    @ManyToOne
    @MapsId("recipeID")
    var recipe: Recipe
)