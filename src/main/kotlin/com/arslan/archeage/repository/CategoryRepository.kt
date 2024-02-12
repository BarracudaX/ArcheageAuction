package com.arslan.archeage.repository

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category,Long>{

    fun findAllByArcheageServer(archeageServer: ArcheageServer) : List<Category>

}