package com.arslan.price.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable
import java.time.Instant

@SequenceGenerators(SequenceGenerator(name = "ITEM_PRICES_SEQUENCE_GENERATOR", sequenceName = "ITEM_PRICES_ID_SEQUENCE_GENERATOR"))
@Entity
@Table(name = "ITEM_PRICES", uniqueConstraints = [UniqueConstraint(name = "ITEM_PRICES_ITEM_NAME_UNIQUE_CONSTRAINT", columnNames = ["itemName","timestamp"])])
@Inheritance(strategy = InheritanceType.JOINED)
open class ItemPrice(
    val itemName: String,

    val price: Price,

    val timestamp: Instant = Instant.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEM_PRICES_ID_SEQUENCE_GENERATOR")
    var id: Long? = null
)