package com.arslan.data.repository

import com.arslan.data.entity.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item,Long>{

    fun findByName(name: String) : Item

}