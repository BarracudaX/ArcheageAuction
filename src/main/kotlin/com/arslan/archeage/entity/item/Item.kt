package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.ArcheageServer
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed

@Indexed
@Entity
@Table(name = "items")
@Inheritance(strategy = InheritanceType.JOINED)
open class Item(

    @FullTextField(name = "name")
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