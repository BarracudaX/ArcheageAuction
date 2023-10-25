package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO

interface PackService {

    fun packs(continent: Continent) : List<PackDTO>

}