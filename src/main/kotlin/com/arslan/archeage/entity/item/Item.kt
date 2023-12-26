package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.ArcheageServer
import jakarta.persistence.*

@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.JOINED)
open class Item(

    @Column
    open var name: String,

    @Column(columnDefinition = "TEXT")
    open var description: String,

    @ManyToOne
    var archeageServer: ArcheageServer,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    open var id: Long? = null
)