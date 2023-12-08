package com.arslan.archeage.repository

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.ItemRecipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemRecipeRepository : JpaRepository<ItemRecipe,Long> {

    @Query("SELECT distinct m.item FROM ItemRecipe r JOIN r.materials m")
    fun findAllCraftingMaterials(pageable: Pageable) : Page<Item>

}