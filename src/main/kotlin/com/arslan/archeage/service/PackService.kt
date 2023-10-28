package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.Pack

interface PackService {

    fun packs(continent: Continent) : List<Pack>

    fun packs(continent: Continent,departureLocation: String,destinationLocation: String) : List<Pack>

    fun packsAt(continent: Continent,departureLocation: String) : List<Pack>

    fun packsTo(continent: Continent,destinationLocation: String) : List<Pack>


}