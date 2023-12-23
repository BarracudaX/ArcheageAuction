package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ItemService {


    fun purchasableItems(pageable: Pageable, archeageServer: ArcheageServer) : Page<PurchasableItem>

}