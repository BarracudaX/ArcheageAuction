package com.arslan.archeage.repository

import com.arslan.archeage.entity.Region
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.pack.PackRecipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRecipeRepository : JpaRepository<PackRecipe,Long> {


    @Query("SELECT distinct m.item FROM PackRecipe r JOIN r.materials m WHERE r.craftable.creationLocation.region = :region AND TYPE(m.item) = PurchasableItem ")
    fun findAllPurchasableCraftingMaterials(pageable: Pageable, region: Region) : Page<Item>

}