package com.arslan.archeage.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table


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