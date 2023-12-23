package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.repository.UserPriceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemPriceServiceImpl(private val userPriceRepository: UserPriceRepository) : ItemPriceService {

    override fun latestPrices(items: List<PurchasableItem>,archeageServer: ArcheageServer): List<UserPrice> = userPriceRepository.latestPrices(items,archeageServer)
    override fun userPrices(items: List<PurchasableItem>, userID: Long,archeageServer: ArcheageServer): Map<Long, UserPrice> = userPriceRepository.userPrices(items,userID,archeageServer).associateBy { it.id.purchasableItem.id!! }

}