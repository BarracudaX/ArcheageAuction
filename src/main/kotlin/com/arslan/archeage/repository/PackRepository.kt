package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.pack.Pack
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackRepository : JpaRepository<Pack,Long>{
    @Query("SELECT distinct p FROM Pack p JOIN FETCH p.creationLocation JOIN FETCH p.price LEFT JOIN FETCH p.materials m LEFT JOIN FETCH m.item WHERE p.id IN (:ids)")
    fun packs(ids: Collection<Long>) : List<Pack>

    @Query("SELECT p.id FROM Pack p WHERE p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent AND :destinationLocation = p.price.sellLocation.id AND p.creationLocation.id = :departureLocation")
    fun packsIDS(pageable: Pageable,server: ArcheageServer,continent: Continent,departureLocation: Long,destinationLocation: Long) : Page<Long>

    @Query("SELECT p.id FROM Pack p WHERE p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent AND :destinationLocation = p.price.sellLocation.id")
    fun packsToIDs(pageable: Pageable,server: ArcheageServer,continent: Continent,destinationLocation: Long) : Page<Long>

    @Query("SELECT p.id FROM Pack p WHERE p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent AND p.creationLocation.id = :departureLocation")
    fun packsAtIDs(pageable: Pageable,server: ArcheageServer,continent: Continent,departureLocation: Long) : Page<Long>

    @Query("SELECT p.id FROM Pack p WHERE  p.creationLocation.archeageServer = :server AND p.creationLocation.continent = :continent")
    fun allPacksIDs(pageable: Pageable,server: ArcheageServer, continent: Continent) : Page<Long>

}