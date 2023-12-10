package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.User
import jakarta.persistence.*
import java.time.Instant


@Table(name = "user_prices")
@Entity
class UserPrice(

    @JoinColumn(name = "item_id")
    @ManyToOne(optional = false)
    var purchasableItem: PurchasableItem,

    @ManyToOne(optional = false)
    var archeageServer: ArcheageServer,

    var price: Price,

    @ManyToOne
    var user: User,

    var timestamp: Instant = Instant.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null
)