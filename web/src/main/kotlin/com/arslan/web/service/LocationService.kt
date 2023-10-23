package com.arslan.web.service

import com.arslan.web.Continent
import com.arslan.web.LocationDTO

interface LocationService {

    fun locations(continent: Continent) : List<LocationDTO>

    fun factories(continent: Continent) : List<LocationDTO>

}