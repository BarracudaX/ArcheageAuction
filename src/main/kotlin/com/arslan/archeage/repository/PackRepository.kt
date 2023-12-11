package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.pack.Pack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRepository : JpaRepository<Pack,Long>{

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent")
    fun allPacks(server: ArcheageServer, continent: Continent) : List<Pack>

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent AND p.creationLocation.id = :departureLocation")
    fun packsAt(server: ArcheageServer,continent: Continent,departureLocation: Long) : List<Pack>

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent AND pr.sellLocation.id = :destinationLocation ")
    fun packsTo(server: ArcheageServer,continent: Continent,destinationLocation: Long) : List<Pack>

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent AND pr.sellLocation.id = :destinationLocation AND p.creationLocation.id = :departureLocation")
    fun packs(server: ArcheageServer, continent: Continent, departureLocation: Long, destinationLocation: Long) : List<Pack>
}