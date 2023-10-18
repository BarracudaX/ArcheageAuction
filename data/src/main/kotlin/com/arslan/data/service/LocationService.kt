package com.arslan.data.service

import com.arslan.data.entity.Location

interface LocationService {

    fun locations() : List<Location>

}