package com.arslan.data.repository

import com.arslan.data.entity.Continent
import com.arslan.data.entity.Location
import com.arslan.data.entity.Pack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRepository : JpaRepository<Pack,Long>{

    fun findByCreationLocation(creationLocation: Location) : List<Pack>


    @Query("SELECT p FROM Pack p JOIN FETCH p.recipes r JOIN FETCH r.materials WHERE p.creationLocation.continent = :continent")
    fun findByContinent(continent: Continent) : List<Pack>

}