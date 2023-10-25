package com.arslan.archeage.entity

import com.arslan.archeage.Continent
import jakarta.persistence.*

@Table(name = "LOCATIONS")
@Entity
class Location(
    val name: String,

    @Enumerated(EnumType.STRING)
    val continent: Continent,

    @Enumerated(EnumType.STRING)
    val region: Region,

    val hasFactory: Boolean = false,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATIONS_SEQUENCE_GENERATOR")
    var id: Long? = null
)