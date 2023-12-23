package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.repository.PurchasableItemRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemServiceImpl(private val purchasableItemRepository: PurchasableItemRepository) : ItemService {
    override fun purchasableItems(pageable: Pageable, archeageServer: ArcheageServer): Page<PurchasableItem> = purchasableItemRepository.findPurchasableItems(pageable, archeageServer)
}