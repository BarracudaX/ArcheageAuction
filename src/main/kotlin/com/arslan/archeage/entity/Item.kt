package com.arslan.archeage.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "ITEMS", uniqueConstraints = [UniqueConstraint(name = "ITEMS_UNIQUE_NAME_CONSTRAINT", columnNames = ["name"])])
@Inheritance(strategy = InheritanceType.JOINED)
open class Item(
    @Column
    open var name: String,

    @Column(columnDefinition = "TEXT")
    open var description: String,

    @Enumerated(EnumType.STRING)
    open var region: Region,

    @OneToMany(mappedBy = "craftable")
    open var recipes: MutableSet<Recipe> = mutableSetOf(),


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_SEQUENCE_GENERATOR")
    open var id: Long? = null
){

    @OneToMany(mappedBy = "item")
    private var prices: MutableSet<ItemPrice> = mutableSetOf()

    open fun addPrice(price: ItemPrice){
        prices.add(price)
    }

    fun prices() : Set<ItemPrice> = Collections.unmodifiableSet(prices)

}