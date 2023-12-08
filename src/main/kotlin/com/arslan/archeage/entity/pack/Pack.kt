package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.Location
import jakarta.persistence.*

@Entity
@Table(name = "packs")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    var name: String,

    var description: String,

    @OneToMany(mappedBy = "craftable")
    var recipes: MutableSet<PackRecipe> = mutableSetOf(),

    @JoinColumn(name = "pack_id")
    @CollectionTable(name = "pack_prices")
    @ElementCollection
    var prices: MutableSet<PackPrice> = mutableSetOf(),

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    var id: Long? = null
)