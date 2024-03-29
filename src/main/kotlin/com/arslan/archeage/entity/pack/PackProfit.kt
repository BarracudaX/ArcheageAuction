package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.Price
import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.Instant

@Table(name = "pack_profits")
@Entity
class PackProfit(
    @EmbeddedId
    val id: PackProfitKey,

    @AttributeOverrides(
        AttributeOverride(name = "gold", column = Column(name = "gold_net_profit")),
        AttributeOverride(name = "silver", column = Column(name = "silver_net_profit")),
        AttributeOverride(name = "copper", column = Column(name = "copper_net_profit"))
    )
    var netProfit: Price,

    @AttributeOverrides(
        AttributeOverride(name = "gold", column = Column(name = "gold_working_points_profit")),
        AttributeOverride(name = "silver", column = Column(name = "silver_working_points_profit")),
        AttributeOverride(name = "copper", column = Column(name = "copper_working_points_profit"))
    )
    var workingPointsProfit: Price,

    var percentage: Int = 100,

    var timestamp: Instant = Instant.now()
)