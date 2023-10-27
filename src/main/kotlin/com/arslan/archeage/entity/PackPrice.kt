package com.arslan.archeage.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "pack_prices")
@Entity
class PackPrice(
    item: Pack,

    archeageServer: ArcheageServer,

    price: Price,

    @ManyToOne(optional = false)
    var sellLocation: Location
) : ItemPrice(item, archeageServer, price)