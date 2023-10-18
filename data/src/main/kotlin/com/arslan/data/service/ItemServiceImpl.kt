package com.arslan.data.service

import com.arslan.data.entity.Item
import com.arslan.data.repository.ItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ItemServiceImpl(private val itemRepository: ItemRepository) : ItemService {

    @Transactional(readOnly = true)
    override fun items(): List<Item> = itemRepository.findAll()

}