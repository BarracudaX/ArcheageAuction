package com.arslan.archeage.repository

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserPriceRepository : JpaRepository<UserPrice,UserPriceKey> {


    @Query("SELECT distinct p FROM UserPrice p WHERE p.id.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.id.purchasableItem = p.id.purchasableItem)")
    fun latestPrices(items: Collection<Item>) : List<UserPrice>

    @Query("SELECT distinct p FROM UserPrice p WHERE p.id.purchasableItem in :items AND p.timestamp = (SELECT max(p2.timestamp) FROM UserPrice p2 WHERE p2.id.purchasableItem = p.id.purchasableItem and p.id.user.id = :userID)")
    fun userItemPrices(items: Collection<PurchasableItem>, userID: Long) : List<UserPrice>

    @Query("SELECT u FROM UserPrice u WHERE u.id.user.id = :userID")
    fun findByUserID(userID: Long, pageable: Pageable) : Page<UserPrice>
}