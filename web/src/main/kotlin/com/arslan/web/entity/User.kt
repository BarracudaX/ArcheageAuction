package com.arslan.web.entity

import jakarta.persistence.*

@SequenceGenerators(SequenceGenerator(name = "USERS_SEQUENCE_GENERATOR", sequenceName = "USERS_ID_SEQUENCE_GENERATOR"))
@Table(name = "USERS")
@Entity
class User(
    var email: String,

    var password: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQUENCE_GENERATOR")
    var id: Long? = null
)