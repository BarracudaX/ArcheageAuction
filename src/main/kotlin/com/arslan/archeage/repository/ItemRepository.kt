package com.arslan.archeage.repository

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ItemRepository : JpaRepository<Item,Long>