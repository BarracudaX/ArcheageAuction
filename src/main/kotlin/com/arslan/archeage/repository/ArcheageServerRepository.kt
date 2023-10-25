package com.arslan.archeage.repository

import com.arslan.archeage.entity.Region
import com.arslan.archeage.entity.ArcheageServer
import org.springframework.data.jpa.repository.JpaRepository

interface ArcheageServerRepository : JpaRepository<ArcheageServer,Long>{

    fun findByRegion(region: Region) : List<ArcheageServer>

}