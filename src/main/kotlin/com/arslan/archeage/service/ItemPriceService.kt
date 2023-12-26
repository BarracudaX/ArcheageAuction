package com.arslan.archeage.service

import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ItemPriceService {

    fun prices(items: List<PurchasableItem>) : List<UserPrice>

    fun userItemPrices(items: List<PurchasableItem>, userID: Long) : Map<Long,UserPrice>
    fun saveUserPrice(userPriceDTO: UserPriceDTO)

    fun userPrices(userID: Long,pageable: Pageable) : Page<UserPrice>
}