package com.arslan.archeage.entity

import jakarta.persistence.*

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

    @OneToMany(mappedBy = "item")
    open var prices: MutableSet<ItemPrice> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_SEQUENCE_GENERATOR")
    open var id: Long? = null
)