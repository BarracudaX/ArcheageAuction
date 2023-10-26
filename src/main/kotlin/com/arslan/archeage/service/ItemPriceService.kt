package com.arslan.archeage.service

import com.arslan.archeage.entity.Item
import com.arslan.archeage.entity.ItemPrice

interface ItemPriceService {

    fun latestPrices(items: List<Item>) : List<ItemPrice>

}