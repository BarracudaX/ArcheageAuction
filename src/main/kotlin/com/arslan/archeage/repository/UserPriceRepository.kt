package com.arslan.archeage.repository

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.UserPrice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserPriceRepository : JpaRepository<UserPrice,Long> {


    @Query("SELECT distinct p FROM UserPrice p JOIN FETCH p.archeageServer JOIN FETCH p.purchasableItem WHERE p.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.purchasableItem = p.purchasableItem AND p2.archeageServer=:archeageServer) AND p.archeageServer = :archeageServer")
    fun latestPrices(items: Collection<Item>,archeageServer: ArcheageServer) : List<UserPrice>

    @Query("SELECT distinct p FROM UserPrice p JOIN FETCH p.archeageServer JOIN FETCH p.purchasableItem WHERE p.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.purchasableItem = p.purchasableItem and p.user.id = :userID AND p2.archeageServer=:archeageServer) AND p.archeageServer = :archeageServer")
    fun userPrices(items: Collection<Item>, userID: Long,archeageServer: ArcheageServer) : List<UserPrice>
}