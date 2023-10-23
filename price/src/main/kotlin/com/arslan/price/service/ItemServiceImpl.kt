package com.arslan.price.service

import com.arslan.price.entity.ItemPrice
import com.arslan.price.repository.ItemPriceRepository
import com.arslan.price.repository.PackPriceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemServiceImpl(private val itemPriceRepository: ItemPriceRepository,private val packPriceRepository: PackPriceRepository) : ItemService {

    override fun latestPrices(names: Collection<String>): List<ItemPrice> = itemPriceRepository.findByNames(names).plus(packPriceRepository.findByItemNameIn(names))
}