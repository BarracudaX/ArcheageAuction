package com.arslan.archeage.entity.item

import com.arslan.archeage.entity.Region
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.BatchSize
import java.util.*

@Table(name = "purchasable_items")
@Entity
open class PurchasableItem(
    name: String,

    description: String,

    region: Region,

) : Item(name, description, region){

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "purchasableItem")
    private var prices: MutableCollection<UserPrice> = mutableListOf()

    fun addPrice(userPrice: UserPrice){
        if(userPrice.archeageServer.region != region)
            throw IllegalArgumentException("Cannot add user price that is defined for archeage server with id ${userPrice.archeageServer.id} because server's region ${userPrice.archeageServer.region} is different from item's region $region.")
        prices.add(userPrice)
    }

    fun prices() : Collection<UserPrice> = Collections.unmodifiableCollection(prices)

}