package com.arslan.web.service

import com.arslan.web.LocationDTO

interface LocationService {

    fun locations() : List<LocationDTO>

}