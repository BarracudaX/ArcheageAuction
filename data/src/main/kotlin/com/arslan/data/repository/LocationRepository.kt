package com.arslan.data.repository

import com.arslan.data.entity.Continent
import com.arslan.data.entity.Location
import org.springframework.data.jpa.repository.JpaRepository

interface LocationRepository : JpaRepository<Location,Long>{

    fun findByName(name: String) : Location

    fun findByContinent(continent: Continent) : List<Location>

    fun findByContinentAndHasFactoryIsTrue(continent: Continent) : List<Location>
}