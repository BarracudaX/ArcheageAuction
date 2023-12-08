package com.arslan.archeage.repository

import com.arslan.archeage.entity.item.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item,Long>