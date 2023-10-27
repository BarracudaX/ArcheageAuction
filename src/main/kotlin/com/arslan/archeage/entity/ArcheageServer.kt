package com.arslan.archeage.entity

import jakarta.persistence.*

@Table(name = "archeage_servers")
@Entity
class ArcheageServer(
    var name: String,

    @Enumerated(EnumType.STRING)
    var region: Region,

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArcheageServer

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "ArcheageServer(name='$name', region=$region, id=$id)"
    }

}