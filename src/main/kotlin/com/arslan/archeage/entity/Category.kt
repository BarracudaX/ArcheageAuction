package com.arslan.archeage.entity

import jakarta.persistence.*

@Entity
@Table(name = "categories")
open class Category(
    var name: String,

    @ManyToOne
    @JoinColumn(name = "parent_category")
    var parent: Category? = null,

    @ManyToOne
    var archeageServer: ArcheageServer,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null
)

