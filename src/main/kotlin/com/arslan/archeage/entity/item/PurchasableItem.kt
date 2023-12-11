package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.ArcheageServer
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "purchasable_items")
@Entity
open class PurchasableItem(
    name: String,

    description: String,

    archeageServer: ArcheageServer
) : Item(name, description,archeageServer)