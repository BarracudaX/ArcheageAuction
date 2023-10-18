package com.arslan.data.entity

import jakarta.persistence.*
import org.hibernate.Length

@Entity
@SequenceGenerators(SequenceGenerator(name = "ITEMS_SEQUENCE_GENERATOR", sequenceName = "ITEMS_ID_SEQUENCE_GENERATOR"))
@Table(name = "ITEMS", uniqueConstraints = [UniqueConstraint(name = "ITEMS_UNIQUE_NAME_CONSTRAINT", columnNames = ["name"])])
@Inheritance(strategy = InheritanceType.JOINED)
open class Item(
    @Column
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ITEMS_SEQUENCE_GENERATOR")
    var id: Long? = null
)