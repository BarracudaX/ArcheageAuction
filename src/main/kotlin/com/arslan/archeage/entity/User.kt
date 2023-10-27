package com.arslan.archeage.entity

import jakarta.persistence.*

@Table(name = "USERS")
@Entity
class User(
    var email: String,

    var password: String,

    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null
)