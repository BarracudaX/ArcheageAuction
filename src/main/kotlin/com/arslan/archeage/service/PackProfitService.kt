package com.arslan.archeage.service

import com.arslan.archeage.PackPercentageUpdate
import com.arslan.archeage.event.ItemPriceChangeEvent

interface PackProfitService : ItemPriceChangeEventListener {

    fun updatePercentage(update: PackPercentageUpdate)
}