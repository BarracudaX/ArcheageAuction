package com.arslan.archeage.repository

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.pack.PackRecipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRecipeRepository : JpaRepository<PackRecipe,Long> {


    @Query("SELECT distinct m.item FROM PackRecipe r JOIN r.materials m")
    fun findAllCraftingMaterials(pageable: Pageable) : Page<Item>

}