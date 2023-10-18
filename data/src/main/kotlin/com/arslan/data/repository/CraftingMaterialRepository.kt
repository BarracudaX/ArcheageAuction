package com.arslan.data.repository

import com.arslan.data.entity.CraftingMaterial
import com.arslan.data.entity.CraftingMaterialID
import org.springframework.data.jpa.repository.JpaRepository

interface CraftingMaterialRepository : JpaRepository<CraftingMaterial, CraftingMaterialID>