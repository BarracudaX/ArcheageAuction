package com.arslan.archeage.service

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.UserPrice

interface ItemPriceService {

    fun latestPrices(items: List<Item>) : List<UserPrice>

    fun userPrices(items: List<Item>, userID: Long) : Map<Long,UserPrice>

}