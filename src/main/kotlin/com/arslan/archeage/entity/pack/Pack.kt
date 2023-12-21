package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.item.Item
import jakarta.persistence.*
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed

@Entity
@Table(name = "packs")
class Pack(
    @ManyToOne(optional = false)
    var creationLocation: Location,

    name: String,

    description: String,
) : Item(name,description,creationLocation.archeageServer){


    @CollectionTable(name = "pack_prices", joinColumns = [JoinColumn(name = "pack_id")])
    @ElementCollection
    private var prices: MutableSet<PackPrice> = mutableSetOf()

    fun addPrice(price: PackPrice) : Boolean{
        if(price.sellLocation.archeageServer != creationLocation.archeageServer)
            throw IllegalArgumentException("Cannot add pack price with sell location that belongs to different archeage server than the pack's create location.Sell location archeage server id: ${price.sellLocation.archeageServer.id},create location archeage server id: ${creationLocation.archeageServer.id}")

        return prices.add(price)
    }

    fun prices() : Set<PackPrice> = prices

}