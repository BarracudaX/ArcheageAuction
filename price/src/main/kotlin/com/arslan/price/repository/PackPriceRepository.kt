package com.arslan.price.repository

import com.arslan.price.entity.PackPrice
import org.springframework.data.jpa.repository.JpaRepository

interface PackPriceRepository : JpaRepository<PackPrice,Long> {

    fun findByItemNameIn(names: Collection<String>) : List<PackPrice>

}