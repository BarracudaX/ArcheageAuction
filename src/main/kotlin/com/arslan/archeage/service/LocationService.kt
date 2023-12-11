package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location


interface LocationService {

    fun continentLocations(continent: Continent,archeageServer: ArcheageServer) : List<Location>

    fun continentFactories(continent: Continent,archeageServer: ArcheageServer) : List<Location>

}