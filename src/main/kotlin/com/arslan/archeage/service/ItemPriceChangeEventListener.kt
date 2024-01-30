package com.arslan.archeage.service

import com.arslan.archeage.event.ItemPriceChangeEvent
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

interface ItemPriceChangeEventListener {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    fun onItemPriceChange(event: ItemPriceChangeEvent)

}