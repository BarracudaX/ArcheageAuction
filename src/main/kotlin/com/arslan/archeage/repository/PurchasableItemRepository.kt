package com.arslan.archeage.repository

import com.arslan.archeage.entity.item.PurchasableItem
import org.springframework.data.jpa.repository.JpaRepository

interface PurchasableItemRepository : JpaRepository<PurchasableItem,Long>