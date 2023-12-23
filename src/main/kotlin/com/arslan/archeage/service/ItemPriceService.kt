package com.arslan.archeage.service

import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice

interface ItemPriceService {

    fun prices(items: List<PurchasableItem>) : List<UserPrice>

    fun userPrices(items: List<PurchasableItem>, userID: Long) : Map<Long,UserPrice>
    fun saveUserPrice(userPriceDTO: UserPriceDTO)

}