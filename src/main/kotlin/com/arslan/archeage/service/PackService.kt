package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO

interface PackService {

    fun packs(continent: Continent) : List<PackDTO>

    fun packs(continent: Continent,departureLocation: String,destinationLocation: String) : List<PackDTO>

    fun packsCreatedAt(continent: Continent, departureLocation: String) : List<PackDTO>

    fun packsSoldAt(continent: Continent, destinationLocation: String) : List<PackDTO>


}