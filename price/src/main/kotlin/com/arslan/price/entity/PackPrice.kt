package com.arslan.price.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "PACK_PRICES")
class PackPrice(
    itemName: String,

    val destinationLocation: String,

    price: Price
) : ItemPrice(itemName, price)