package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.CraftingMaterial
import jakarta.persistence.*

@Entity
@Table(name = "recipes")
class Recipe(
    @ManyToOne
    var craftable: Item,

    var producedQuantity: Int = 1,


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIPES_SEQUENCE_GENERATOR")
    var id: Long? = null
){

    init {
        craftable.addRecipe(this)
    }


    @CollectionTable(name = "recipe_materials")
    @ElementCollection
    private val materials: MutableSet<CraftingMaterial> = mutableSetOf()

    fun materials() : Set<CraftingMaterial> = materials

    fun addMaterial(material: CraftingMaterial){
        if(material.item.archeageServer != craftable.archeageServer) throw IllegalArgumentException("")
        materials.add(material)
    }

}