package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.repository.ItemRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemServiceImpl(private val itemRepository: ItemRepository) : ItemService {
    override fun purchasableCraftingMaterials(pageable: Pageable, archeageServer: ArcheageServer): Page<Item> = itemRepository.findPurchasableItems(pageable, archeageServer)
}