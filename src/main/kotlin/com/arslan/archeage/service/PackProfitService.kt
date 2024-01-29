package com.arslan.archeage.service

import com.arslan.archeage.PackPercentageUpdate
import com.arslan.archeage.event.ItemPriceChangeEvent

interface PackProfitService {

    fun onItemPriceChange(event: ItemPriceChangeEvent)

    fun updatePercentage(update: PackPercentageUpdate)
}