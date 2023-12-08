package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.item.Item
import jakarta.persistence.*

@Table(name = "pack_recipes")
@Entity
class PackRecipe(
    @ManyToOne
    var craftable: Pack,

    var producedQuantity: Int = 1,

    @CollectionTable(name = "pack_recipe_materials")
    @ElementCollection
    val materials: MutableSet<CraftingMaterial> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null
) {

    init {
        craftable.recipes.add(this)
    }

}