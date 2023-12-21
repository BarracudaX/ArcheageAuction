package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.ArcheageServer
import jakarta.persistence.*
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed

@Indexed
@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.JOINED)
open class Item(

    @FullTextField(name = "name")
    @Column
    open var name: String,

    @Column(columnDefinition = "TEXT")
    open var description: String,

    @ManyToOne
    var archeageServer: ArcheageServer,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    open var id: Long? = null
){

    @OneToMany(mappedBy = "craftable")
    private var recipes: MutableSet<Recipe> = mutableSetOf()

    fun recipes() : Set<Recipe> = recipes

    fun addRecipe(recipe: Recipe){
        recipes.add(recipe)
    }

}