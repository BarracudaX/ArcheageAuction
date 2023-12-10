package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.UserPrice

interface ItemPriceService {

    fun latestPrices(items: List<Item>,archeageServer: ArcheageServer) : List<UserPrice>

    fun userPrices(items: List<Item>, userID: Long,archeageServer: ArcheageServer) : Map<Long,UserPrice>

}