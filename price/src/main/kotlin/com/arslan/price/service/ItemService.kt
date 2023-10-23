package com.arslan.price.service

import com.arslan.price.entity.ItemPrice

interface ItemService {

    fun latestPrices(names: Collection<String>) : List<ItemPrice>

}