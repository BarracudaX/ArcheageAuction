package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.CraftingMaterial
import jakarta.persistence.*

@Entity
@Table(name = "item_recipes")
class ItemRecipe(
    @ManyToOne
    var craftable: Item,

    var producedQuantity: Int = 1,

    @CollectionTable(name = "item_recipe_materials")
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