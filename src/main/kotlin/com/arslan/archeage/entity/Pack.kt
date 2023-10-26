package com.arslan.archeage.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "PACKS")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    name: String,

    description: String,

    region: Region,


) : Item(name, description, region)