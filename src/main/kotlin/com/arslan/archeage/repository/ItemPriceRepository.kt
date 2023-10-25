package com.arslan.archeage.repository

import com.arslan.archeage.entity.ItemPrice
import org.springframework.data.jpa.repository.JpaRepository

interface ItemPriceRepository : JpaRepository<ItemPrice,Long> {
}