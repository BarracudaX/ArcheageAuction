package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.pack.Pack
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRepository : JpaRepository<Pack,Long>{

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH pr.archeageServer JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE pr.archeageServer = :server AND p.creationLocation.continent = :continent")
    fun allPacks(server: ArcheageServer, continent: Continent) : List<Pack>

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH pr.archeageServer JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE pr.archeageServer = :server AND p.creationLocation.continent = :continent AND p.creationLocation.name = :departureLocation")
    fun packsAt(server: ArcheageServer,continent: Continent,departureLocation: String) : List<Pack>

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH pr.archeageServer JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE pr.archeageServer = :server AND p.creationLocation.continent = :continent AND pr.sellLocation.name = :destinationLocation ")
    fun packsTo(server: ArcheageServer,continent: Continent,destinationLocation: String) : List<Pack>

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr JOIN FETCH pr.sellLocation JOIN FETCH pr.archeageServer JOIN FETCH p.creationLocation LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE pr.archeageServer = :server AND p.creationLocation.continent = :continent AND pr.sellLocation.name = :destinationLocation AND p.creationLocation.name = :departureLocation")
    fun packs(server: ArcheageServer, continent: Continent,departureLocation: String,destinationLocation: String) : List<Pack>
}