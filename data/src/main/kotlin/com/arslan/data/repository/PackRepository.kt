package com.arslan.data.repository

import com.arslan.data.entity.Location
import com.arslan.data.entity.Pack
import org.springframework.data.jpa.repository.JpaRepository

interface PackRepository : JpaRepository<Pack,Long>{

    fun findByCreationLocation(creationLocation: Location) : List<Pack>

}