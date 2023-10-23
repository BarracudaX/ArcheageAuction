package com.arslan.data.service

import com.arslan.data.PackDTO
import com.arslan.data.entity.Continent
import com.arslan.data.entity.Pack

interface PackService {

    fun packsAtLocation(location: String) : List<Pack>

    fun packAtContinent(continent: Continent) : List<PackDTO>
}