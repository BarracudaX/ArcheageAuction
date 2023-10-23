package com.arslan.web.service

import com.arslan.web.Continent
import com.arslan.web.PackDTO

interface PackService {

    fun packs(continent: Continent) : List<PackDTO>

}