package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.Location
import jakarta.persistence.*
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed

@Entity
@Table(name = "packs")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    var name: String,

    var description: String,

    @OneToMany(mappedBy = "craftable")
    var recipes: MutableSet<PackRecipe> = mutableSetOf(),

    @CollectionTable(name = "pack_prices", joinColumns = [JoinColumn(name = "pack_id")])
    @ElementCollection
    var prices: MutableSet<PackPrice> = mutableSetOf(),

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null
)