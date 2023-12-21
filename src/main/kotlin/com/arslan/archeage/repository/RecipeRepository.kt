package com.arslan.archeage.repository

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.Recipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RecipeRepository : JpaRepository<Recipe,Long> {

    @Query("SELECT distinct m.item FROM Recipe r JOIN r.materials m")
    fun findAllCraftingMaterials(pageable: Pageable) : Page<Item>

    @Query("SELECT m.item FROM Recipe r JOIN r.materials m WHERE type(m.item) = PurchasableItem ")
    fun findAllPurchasableCraftingMaterials(pageable: Pageable,archeageServer: ArcheageServer) : Page<Item>
}