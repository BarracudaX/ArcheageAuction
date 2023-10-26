package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.Pack

interface PackService {

    fun packs(continent: Continent) : List<Pack>

}