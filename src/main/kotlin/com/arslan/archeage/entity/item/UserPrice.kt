package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.User
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant


@Table(name = "user_prices")
@Entity
class UserPrice(
    @EmbeddedId
    var id: UserPriceKey,

    var price: Price,

    var timestamp: Instant = Instant.now()
)