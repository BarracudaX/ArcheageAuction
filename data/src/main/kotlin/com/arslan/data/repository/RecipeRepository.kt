package com.arslan.data.repository

import com.arslan.data.entity.Item
import com.arslan.data.entity.Recipe
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository : JpaRepository<Recipe,Long> {

    fun findByCraftable(craftable: Item) : List<Recipe>

}