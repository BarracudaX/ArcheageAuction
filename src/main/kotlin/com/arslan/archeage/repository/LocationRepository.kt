package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location,Long>{

    fun findByContinentAndArcheageServer(continent: Continent, archeageServer: ArcheageServer) : List<Location>

    fun findByContinentAndHasFactoryIsTrueAndArcheageServer(continent: Continent, archeageServer: ArcheageServer) : List<Location>

}