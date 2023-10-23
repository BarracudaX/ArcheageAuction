package com.arslan.price.repository

import com.arslan.price.entity.ItemPrice
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemPriceRepository : JpaRepository<ItemPrice,Long> {

    @Query("SELECT i FROM ItemPrice i WHERE i.itemName in (:names) AND i.timestamp = (SELECT MAX(i2.timestamp) FROM ItemPrice i2 WHERE i2.itemName = i.itemName) AND TYPE(i) = ItemPrice")
    fun findByNames(names: Collection<String>) : List<ItemPrice>

}