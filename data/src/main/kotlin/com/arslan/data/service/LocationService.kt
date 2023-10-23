package com.arslan.data.service

import com.arslan.data.entity.Continent
import com.arslan.data.entity.Location

interface LocationService {

    fun continentLocations(continent: Continent) : List<Location>

    fun continentFactories(continent: Continent) : List<Location>
}