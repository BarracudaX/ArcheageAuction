package com.arslan.data.entity

import jakarta.persistence.*

@SequenceGenerators(SequenceGenerator(name = "LOCATIONS_SEQUENCE_GENERATOR", sequenceName = "LOCATIONS_ID_SEQUENCE_GENERATOR"))
@Table(name = "LOCATIONS")
@Entity
class Location(
    val name: String,

    @Enumerated(EnumType.STRING)
    val continent: Continent,

    val hasFactory: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATIONS_SEQUENCE_GENERATOR")
    var id: Long? = null
)