package com.arslan.data.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.MapsId
import java.io.Serializable

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