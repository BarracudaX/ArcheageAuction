package com.arslan.archeage.event

import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.User
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import org.springframework.context.ApplicationEvent

class ItemPriceChangeEvent(source: Any, val item: PurchasableItem,val user: User, val priceChange: Price) : ApplicationEvent(source)