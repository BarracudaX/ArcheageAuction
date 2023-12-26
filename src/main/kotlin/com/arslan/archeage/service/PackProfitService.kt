package com.arslan.archeage.service

import com.arslan.archeage.event.ItemPriceChangeEvent

interface PackProfitService {

    fun onItemPriceChange(event: ItemPriceChangeEvent)

}