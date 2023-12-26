package com.arslan.archeage.service

import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.event.ItemPriceChangeEvent
import com.arslan.archeage.repository.PurchasableItemRepository
import com.arslan.archeage.repository.UserPriceRepository
import com.arslan.archeage.repository.UserRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class ItemPriceServiceImpl(
    private val userPriceRepository: UserPriceRepository,
    private val userRepository: UserRepository,
    private val purchasableItemRepository: PurchasableItemRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) : ItemPriceService {

    override fun prices(items: List<PurchasableItem>): List<UserPrice> = userPriceRepository.latestPrices(items)
    override fun userItemPrices(items: List<PurchasableItem>, userID: Long): Map<Long, UserPrice> = userPriceRepository.userItemPrices(items,userID).associateBy { it.id.purchasableItem.id!! }
    override fun saveUserPrice(userPriceDTO: UserPriceDTO) {
        val user = userRepository.findById(userPriceDTO.userID!!).orElseThrow { EmptyResultDataAccessException(1) }
        val item = purchasableItemRepository.findById(userPriceDTO.itemID).orElseThrow { EmptyResultDataAccessException(1) }

        val previousUserPrice = userPriceRepository.findById(UserPriceKey(user,item)).getOrNull()?.price?.copy() ?: Price(0,0,0)

        val newUserPrice = userPriceRepository.save(UserPrice(UserPriceKey(user,item),userPriceDTO.price))

        val diff = newUserPrice.price - previousUserPrice

        applicationEventPublisher.publishEvent(ItemPriceChangeEvent(this,item,user,diff))
    }

    override fun userPrices(userID: Long, pageable: Pageable): Page<UserPrice> = userPriceRepository.findById_User_Id(userID,pageable)

}