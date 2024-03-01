package com.arslan.archeage.repository

import com.arslan.archeage.Continent
import com.arslan.archeage.PackRequest
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.pack.PackProfit
import com.arslan.archeage.entity.pack.PackProfitKey
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface PackProfitRepository : JpaRepository<PackProfit,PackProfitKey>{

    @Query("""
        SELECT new com.arslan.archeage.repository.PackResult(pr.id.pack.id,pr.percentage) FROM PackProfit pr WHERE pr.id.pack.creationLocation.archeageServer = :archeageServer AND pr.id.pack.creationLocation.continent = :#{#packRequest.continent} 
        AND (:#{#packRequest.destinationLocation} IS NULL OR pr.id.pack.price.sellLocation.id = :#{#packRequest.destinationLocation})  
        AND (:#{#packRequest.departureLocation} IS NULL OR pr.id.pack.creationLocation.id = :#{#packRequest.departureLocation}) 
        AND ((:#{#packRequest.userID} IS NOT NULL AND pr.id.user.id = :#{#packRequest.userID}) OR (pr.id.user.id = (SELECT pr2.id.user.id FROM PackProfit pr2 WHERE pr2.id.pack = pr.id.pack AND pr2.timestamp = (SELECT MAX(pr3.timestamp) FROM PackProfit pr3 WHERE pr3.id.pack = pr.id.pack) )))
        AND (:#{#packRequest.categories.size()} = 0 OR pr.id.pack.category.id IN (:#{#packRequest.categories}))
    """)
    fun packs(pageable: Pageable,packRequest: PackRequest,archeageServer: ArcheageServer) : Page<PackResult>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findAllByIdIn(id: Collection<PackProfitKey>) : List<PackProfit>

    @Query("SELECT p FROM PackProfit p WHERE p.id.pack.id = :packID AND p.id.user.id = :userID")
    fun findPackProfit(packID: Long,userID: Long) : PackProfit

}

data class PackResult(val id: Long,val percentage: Int)