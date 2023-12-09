package com.arslan.archeage.entity.pack

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import jakarta.persistence.*
import org.hibernate.annotations.Parent

@Embeddable
class PackPrice(
    @ManyToOne(optional = false)
    var archeageServer: ArcheageServer,

    var price: Price,

    @ManyToOne(optional = false)
    var sellLocation: Location
){
    init {
        if(!sellLocation.hasFactory) throw IllegalArgumentException("Sell location $sellLocation does not have a factory.")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackPrice

        return sellLocation == other.sellLocation
    }

    override fun hashCode(): Int {
        return sellLocation.hashCode()
    }


}