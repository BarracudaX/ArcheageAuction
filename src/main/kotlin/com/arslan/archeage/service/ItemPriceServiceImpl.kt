package com.arslan.archeage.service

import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.repository.PurchasableItemRepository
import com.arslan.archeage.repository.UserPriceRepository
import com.arslan.archeage.repository.UserRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class ItemPriceServiceImpl(private val userPriceRepository: UserPriceRepository,private val userRepository: UserRepository,private val purchasableItemRepository: PurchasableItemRepository) : ItemPriceService {

    override fun prices(items: List<PurchasableItem>): List<UserPrice> = userPriceRepository.latestPrices(items)
    override fun userPrices(items: List<PurchasableItem>, userID: Long): Map<Long, UserPrice> = userPriceRepository.userPrices(items,userID).associateBy { it.id.purchasableItem.id!! }
    override fun saveUserPrice(userPriceDTO: UserPriceDTO) {
        val user = userRepository.findById(userPriceDTO.userID!!).orElseThrow { EmptyResultDataAccessException(1) }
        val item = purchasableItemRepository.findById(userPriceDTO.itemID).orElseThrow { EmptyResultDataAccessException(1) }

        val previousUserPrice = userPriceRepository.findById(UserPriceKey(user,item))

        val newUserPrice = userPriceRepository.save(UserPrice(UserPriceKey(user,item),userPriceDTO.price))

        val diff = newUserPrice.price - (previousUserPrice.getOrNull()?.price ?: Price(0,0,0))
    }

}