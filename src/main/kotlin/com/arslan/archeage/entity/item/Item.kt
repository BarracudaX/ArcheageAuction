package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.Region
import jakarta.persistence.*
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import java.util.*
@Indexed
@Entity
@Table(name = "items", uniqueConstraints = [UniqueConstraint(name = "ITEMS_UNIQUE_NAME_CONSTRAINT", columnNames = ["name"])])
@Inheritance(strategy = InheritanceType.JOINED)
open class Item(

    @FullTextField(name = "name")
    @Column
    open var name: String,

    @Column(columnDefinition = "TEXT")
    open var description: String,

    @Enumerated(EnumType.STRING)
    open var region: Region,

    @OneToMany(mappedBy = "craftable")
    open var recipes: MutableSet<ItemRecipe> = mutableSetOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    open var id: Long? = null
)