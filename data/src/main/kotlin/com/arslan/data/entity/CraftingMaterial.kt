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

    @ManyToOne
    @MapsId("itemID")
    var item: Item,

    @ManyToOne
    @MapsId("recipeID")
    var recipe: Recipe,

    @EmbeddedId
    var craftingMaterialID: CraftingMaterialID
)

@Embeddable
class CraftingMaterialID(
    var itemID: Long,

    var recipeID: Long
) : Serializable