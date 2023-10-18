package com.arslan.data.entity

import jakarta.persistence.*

@Entity
@SequenceGenerators(SequenceGenerator(name = "ITEMS_SEQUENCE_GENERATOR", sequenceName = "ITEMS_ID_SEQUENCE_GENERATOR"))
@Table(name = "ITEMS")
class Item(
    var name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_SEQUENCE_GENERATOR")
    var id: Long? = null
)