package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.pack.PackProfit
import com.arslan.archeage.entity.pack.PackProfitKey
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PackProfitRepository : JpaRepository<PackProfit,PackProfitKey>{

    @Query("""
        SELECT pr.id.pack.id FROM PackProfit pr WHERE pr.id.pack.creationLocation.archeageServer = :server AND pr.id.pack.creationLocation.continent = :continent AND :destinationLocation = pr.id.pack.price.sellLocation.id AND pr.id.pack.creationLocation.id = :departureLocation 
        AND (( :userID IS NOT NULL AND pr.id.user.id = :userID ) OR (pr.id.user.id = (SELECT pr2.id.user.id FROM PackProfit pr2 WHERE pr2.id.pack = pr.id.pack AND pr2.timestamp = (SELECT MAX(pr3.timestamp) FROM PackProfit pr3 WHERE pr3.id.pack = pr.id.pack) )))
    """)
    fun packIDs(pageable: Pageable, server: ArcheageServer, continent: Continent, departureLocation: Long, destinationLocation: Long, userID: Long?) : Page<Long>

    @Query("""
        SELECT pr.id.pack.id FROM PackProfit pr WHERE pr.id.pack.creationLocation.archeageServer = :server AND pr.id.pack.creationLocation.continent = :continent
        AND (( :userID IS NOT NULL AND pr.id.user.id = :userID ) OR (pr.id.user.id = (SELECT pr2.id.user.id FROM PackProfit pr2 WHERE pr2.id.pack = pr.id.pack AND pr2.timestamp = (SELECT MAX(pr3.timestamp) FROM PackProfit pr3 WHERE pr3.id.pack = pr.id.pack) )))
    """)
    fun allPackIDs(pageable: Pageable,server: ArcheageServer, continent: Continent,userID: Long?) : Page<Long>

    @Query("""
        SELECT pr.id.pack.id FROM PackProfit pr WHERE pr.id.pack.creationLocation.archeageServer = :server AND pr.id.pack.creationLocation.continent = :continent AND pr.id.pack.creationLocation.id = :departureLocation
        AND (( :userID IS NOT NULL AND pr.id.user.id = :userID ) OR (pr.id.user.id = (SELECT pr2.id.user.id FROM PackProfit pr2 WHERE pr2.id.pack = pr.id.pack AND pr2.timestamp = (SELECT MAX(pr3.timestamp) FROM PackProfit pr3 WHERE pr3.id.pack = pr.id.pack) )))
    """)
    fun packsAtIDs(pageable: Pageable,server: ArcheageServer,continent: Continent,departureLocation: Long,userID: Long?) : Page<Long>


    @Query("""
        SELECT pr.id.pack.id FROM PackProfit pr WHERE pr.id.pack.creationLocation.archeageServer = :server AND pr.id.pack.creationLocation.continent = :continent AND :destinationLocation = pr.id.pack.price.sellLocation.id
        AND (( :userID IS NOT NULL AND pr.id.user.id = :userID ) OR (pr.id.user.id = (SELECT pr2.id.user.id FROM PackProfit pr2 WHERE pr2.id.pack = pr.id.pack AND pr2.timestamp = (SELECT MAX(pr3.timestamp) FROM PackProfit pr3 WHERE pr3.id.pack = pr.id.pack) )))
    """)
    fun packsToIDs(pageable: Pageable,server: ArcheageServer,continent: Continent,destinationLocation: Long,userID: Long?) : Page<Long>
}