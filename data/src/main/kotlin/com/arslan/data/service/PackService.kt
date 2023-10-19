package com.arslan.data.service

import com.arslan.data.entity.Pack

interface PackService {

    fun findPacksAtLocation(location: String) : List<Pack>

}