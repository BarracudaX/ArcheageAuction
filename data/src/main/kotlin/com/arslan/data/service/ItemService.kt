package com.arslan.data.service

import com.arslan.data.entity.Item

interface ItemService {

    fun items() : List<Item>

}