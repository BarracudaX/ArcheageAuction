package com.arslan.archeage.service

import com.arslan.archeage.entity.item.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ItemService {

    fun packCraftingMaterials(pageable: Pageable) : Page<Item>

}