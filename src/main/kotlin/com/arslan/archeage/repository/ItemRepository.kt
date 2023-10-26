package com.arslan.archeage.repository

import com.arslan.archeage.entity.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemRepository : JpaRepository<Item,Long>{


}