package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Pack
import com.arslan.archeage.entity.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRepository : JpaRepository<Pack,Long>{

    @Query("SELECT distinct p FROM Pack p JOIN FETCH p.prices pr JOIN FETCH p.recipes r JOIN FETCH r.materials m JOIN FETCH m.item WHERE p.region = :region AND pr.archeageServer = :server")
    fun packs(region: Region,server: ArcheageServer) : List<Pack>

}