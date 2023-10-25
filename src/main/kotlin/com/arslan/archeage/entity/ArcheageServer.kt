package com.arslan.archeage.entity

import jakarta.persistence.*

@Entity
class ArcheageServer(
    var name: String,

    @Enumerated(EnumType.STRING)
    var region: Region,

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null
)