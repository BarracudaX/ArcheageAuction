package com.arslan.archeage.repository

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserPriceRepository : JpaRepository<UserPrice,UserPriceKey> {


    @Query("SELECT distinct p FROM UserPrice p WHERE p.id.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.id.purchasableItem = p.id.purchasableItem AND p2.id.purchasableItem.archeageServer=:archeageServer) AND p.id.purchasableItem.archeageServer = :archeageServer")
    fun latestPrices(items: Collection<Item>,archeageServer: ArcheageServer) : List<UserPrice>

    @Query("SELECT distinct p FROM UserPrice p WHERE p.id.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.id.purchasableItem = p.id.purchasableItem and p.id.user.id = :userID AND p2.id.purchasableItem.archeageServer=:archeageServer) AND p.id.purchasableItem.archeageServer = :archeageServer")
    fun userPrices(items: Collection<PurchasableItem>, userID: Long,archeageServer: ArcheageServer) : List<UserPrice>
}