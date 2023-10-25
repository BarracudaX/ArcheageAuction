package com.arslan.archeage.repository

import com.arslan.archeage.entity.Recipe
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository : JpaRepository<Recipe,Long> {
}