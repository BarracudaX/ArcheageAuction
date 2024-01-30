package com.arslan.archeage.service

import com.arslan.archeage.PackPercentageUpdate
import com.arslan.archeage.event.ItemPriceChangeEvent
import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated

@Validated
interface PackProfitService : ItemPriceChangeEventListener {

    fun updatePercentage(@Valid update: PackPercentageUpdate)
}