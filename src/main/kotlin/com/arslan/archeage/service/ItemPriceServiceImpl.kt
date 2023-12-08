package com.arslan.archeage.service

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.repository.UserPriceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemPriceServiceImpl(private val userPriceRepository: UserPriceRepository) : ItemPriceService {

    override fun latestPrices(items: List<Item>): List<UserPrice> = userPriceRepository.latestPrices(items,ArcheageServerContextHolder.getServerContext()!!)
    override fun userPrices(items: List<Item>, userID: Long): Map<Long, UserPrice> = userPriceRepository.userPrices(items,userID,ArcheageServerContextHolder.getServerContext()!!).associateBy { it.purchasableItem.id!! }

}