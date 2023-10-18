package com.arslan.data.repository

import com.arslan.data.entity.Recipe
import org.springframework.data.jpa.repository.JpaRepository

interface RecipeRepository : JpaRepository<Recipe,Long> {
}