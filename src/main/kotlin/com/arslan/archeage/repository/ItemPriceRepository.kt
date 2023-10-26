package com.arslan.archeage.repository

import com.arslan.archeage.entity.ItemPrice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemPriceRepository : JpaRepository<ItemPrice,Long> {


    @Query("SELECT p FROM ItemPrice p WHERE p.id in :ids AND p.timestamp = (SELECT max(p2.timestamp) FROM ItemPrice p2)")
    fun latestPrices(ids: Collection<Long>) : List<ItemPrice>

}