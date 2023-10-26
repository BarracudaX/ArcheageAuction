package com.arslan.archeage.service

import com.arslan.archeage.entity.Item
import com.arslan.archeage.entity.ItemPrice
import com.arslan.archeage.repository.ItemPriceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemPriceServiceImpl(private val itemPriceRepository: ItemPriceRepository) : ItemPriceService {

    override fun latestPrices(items: List<Item>): List<ItemPrice> = itemPriceRepository.latestPrices(items)

}