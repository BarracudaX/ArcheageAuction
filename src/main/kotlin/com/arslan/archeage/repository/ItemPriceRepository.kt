package com.arslan.archeage.repository

import com.arslan.archeage.entity.Item
import com.arslan.archeage.entity.ItemPrice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemPriceRepository : JpaRepository<ItemPrice,Long> {


    @Query("SELECT p FROM ItemPrice p WHERE p.item in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM ItemPrice p2)")
    fun latestPrices(items: Collection<Item>) : List<ItemPrice>

}