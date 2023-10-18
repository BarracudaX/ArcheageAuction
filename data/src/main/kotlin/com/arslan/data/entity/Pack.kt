package com.arslan.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "PACKS")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    name: String,

    description: String
) : Item(name, description)