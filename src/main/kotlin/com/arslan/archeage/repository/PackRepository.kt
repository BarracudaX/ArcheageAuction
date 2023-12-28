package com.arslan.archeage.repository

import com.arslan.archeage.entity.User
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.pack.Pack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRepository : JpaRepository<Pack,Long>{
    @Query("SELECT distinct p FROM Pack p JOIN FETCH p.creationLocation JOIN FETCH p.price LEFT JOIN FETCH p.materials m LEFT JOIN FETCH m.item WHERE p.id IN (:ids)")
    fun packs(ids: Collection<Long>) : List<Pack>

    @Query("""
        SELECT p1 FROM Pack p1 JOIN p1.materials m1 WHERE :item IN (SELECT m2.item FROM Pack p2 JOIN p2.materials m2 WHERE p2.id = p1.id)
        AND NOT EXISTS ( SELECT 1 FROM Pack p3 JOIN p3.materials m3 WHERE p3.id = p1.id AND type(m3.item) = PurchasableItem AND NOT EXISTS (SELECT 1 FROM UserPrice up WHERE up.id.purchasableItem.id = m3.item.id AND up.id.user = :user) )
    """)
    fun selectPacksWithCraftingMaterial(item: Item, user: User) : List<Pack>

}