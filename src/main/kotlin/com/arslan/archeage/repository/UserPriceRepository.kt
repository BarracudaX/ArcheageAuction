package com.arslan.archeage.repository

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.UserPrice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserPriceRepository : JpaRepository<UserPrice,Long> {


    @Query("SELECT distinct p FROM UserPrice p JOIN FETCH p.archeageServer JOIN FETCH p.purchasableItem WHERE p.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.purchasableItem = p.purchasableItem)")
    fun latestPrices(items: Collection<Item>) : List<UserPrice>

    @Query("SELECT distinct p FROM UserPrice p JOIN FETCH p.archeageServer JOIN FETCH p.purchasableItem WHERE p.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.purchasableItem = p.purchasableItem and p.user.id = :userID)")
    fun userPrices(items: Collection<Item>, userID: Long) : List<UserPrice>
}