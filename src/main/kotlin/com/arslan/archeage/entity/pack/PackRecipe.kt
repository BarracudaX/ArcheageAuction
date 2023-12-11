package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.item.Item
import jakarta.persistence.*
import java.util.Collections

@Table(name = "pack_recipes")
@Entity
class PackRecipe(
    @ManyToOne
    var craftable: Pack,

    var producedQuantity: Int = 1,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null
) {

    init {
        craftable.recipes.add(this)
    }


    @CollectionTable(name = "pack_recipe_materials")
    @ElementCollection
    private val materials: MutableSet<CraftingMaterial> = mutableSetOf()

    fun addMaterial(material: CraftingMaterial){
        if(material.item.archeageServer != craftable.creationLocation.archeageServer) throw IllegalArgumentException("Cannot add crafting material that belongs to a different archeage server than the craftable item.")
        materials.add(material)
    }

    fun materials() : Set<CraftingMaterial> = Collections.unmodifiableSet(materials)

}