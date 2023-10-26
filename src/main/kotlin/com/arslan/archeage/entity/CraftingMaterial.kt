package com.arslan.archeage.entity

import jakarta.persistence.*

@Embeddable
class CraftingMaterial(
    var quantity: Int,

    @ManyToOne
    var item: Item,
)