package com.arslan.data.entity

import jakarta.persistence.*

@SequenceGenerators(SequenceGenerator(name = "RECIPES_SEQUENCE_GENERATOR", sequenceName = "RECIPES_ID_SEQUENCE_GENERATOR"))
@Entity
@Table(name = "RECIPES")
class ItemRecipe(
    @ManyToOne
    var item: Item,

    @JoinTable(name = "ITEM_RECIPES")
    @ManyToMany
    var items: MutableSet<Item> = mutableSetOf(),
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIPES_SEQUENCE_GENERATOR")
    var id: Long? = null
)