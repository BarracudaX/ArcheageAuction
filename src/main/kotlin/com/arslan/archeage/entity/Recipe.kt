package com.arslan.archeage.entity

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
@Table(name = "RECIPES")
class Recipe(
    @ManyToOne
    var craftable: Item,

    var producedQuantity: Int = 1,

    @ElementCollection
    val materials: MutableSet<CraftingMaterial> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIPES_SEQUENCE_GENERATOR")
    var id: Long? = null
){

    init {
        craftable.recipes.add(this)
    }

}