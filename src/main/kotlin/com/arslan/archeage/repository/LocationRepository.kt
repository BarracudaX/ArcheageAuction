package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.Location
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location,Long>{

    fun findByName(name: String) : Location

    fun findByContinent(continent: Continent) : List<Location>

    fun findByContinentAndHasFactoryIsTrue(continent: Continent) : List<Location>

}