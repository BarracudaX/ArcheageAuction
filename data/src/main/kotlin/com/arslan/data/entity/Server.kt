package com.arslan.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "SERVERS")
@Entity
class Server(
    @Id
    var name: String
)