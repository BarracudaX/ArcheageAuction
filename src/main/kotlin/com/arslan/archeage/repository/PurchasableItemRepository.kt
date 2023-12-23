package com.arslan.archeage.repository

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PurchasableItemRepository : JpaRepository<PurchasableItem,Long>{

    @Query("SELECT i FROM PurchasableItem i WHERE i.archeageServer = :archeageServer")
    fun findPurchasableItems(pageable: Pageable, archeageServer: ArcheageServer) : Page<PurchasableItem>

}