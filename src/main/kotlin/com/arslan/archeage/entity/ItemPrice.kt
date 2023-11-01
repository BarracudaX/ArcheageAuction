package com.arslan.archeage.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "item_prices")
@Inheritance(strategy = InheritanceType.JOINED)
open class ItemPrice(
    @ManyToOne(optional = false)
    open var item: Item,

    @ManyToOne(optional = false)
    open var archeageServer: ArcheageServer,

    open var price: Price,

    open var timestamp: Instant = Instant.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    open var id: Long? = null
){

    init {
        item.addPrice(this)
    }

}