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
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Location(name='$name', continent=$continent, region=$region, hasFactory=$hasFactory, id=$id)"
    }


}