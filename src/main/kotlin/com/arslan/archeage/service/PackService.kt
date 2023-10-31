package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.ItemPrice
import com.arslan.archeage.entity.Pack

interface PackService {

    fun packs(continent: Continent) : List<PackDTO>

    fun packs(continent: Continent,departureLocation: String,destinationLocation: String) : List<PackDTO>

    fun packsAt(continent: Continent,departureLocation: String) : List<PackDTO>

    fun packsTo(continent: Continent,destinationLocation: String) : List<PackDTO>


}