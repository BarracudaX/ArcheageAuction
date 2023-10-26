package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Pack
import com.arslan.archeage.entity.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRepository : JpaRepository<Pack,Long>{

    @Query("SELECT distinct p FROM Pack p LEFT JOIN FETCH p.prices pr LEFT JOIN FETCH p.recipes r LEFT JOIN FETCH r.materials m LEFT JOIN FETCH m.item WHERE p.region = :region AND pr.archeageServer = :server AND p.creationLocation.continent = :continent")
    fun packs(region: Region,server: ArcheageServer,continent: Continent) : List<Pack>

}