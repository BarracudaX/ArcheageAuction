package com.arslan.archeage.entity

import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

@Entity
class PackPrice(
    item: Item,

    archeageServer: ArcheageServer,

    price: Price,

    @ManyToOne(optional = false)
    var sellLocation: Location
) : ItemPrice(item, archeageServer, price)