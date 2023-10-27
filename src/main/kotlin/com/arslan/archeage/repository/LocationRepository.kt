package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Region
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location,Long>{

    fun findByName(name: String) : Location

    fun findByContinentAndRegion(continent: Continent, region: Region) : List<Location>

    fun findByContinentAndHasFactoryIsTrueAndRegion(continent: Continent, region: Region) : List<Location>

}