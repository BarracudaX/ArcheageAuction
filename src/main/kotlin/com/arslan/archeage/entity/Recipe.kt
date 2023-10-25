package com.arslan.archeage.entity

import jakarta.persistence.*

@Entity
@Table(name = "RECIPES")
class Recipe(
    @ManyToOne
    var craftable: Item,

    var producedQuantity: Int = 1,

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL])
    val materials: MutableSet<CraftingMaterial> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIPES_SEQUENCE_GENERATOR")
    var id: Long? = null
){

    fun addCraftingMaterial(item: Item,quantity: Int){
        val material = CraftingMaterial(quantity,item,this)
        materials.add(material)
    }

}