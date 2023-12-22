package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.item.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PackService {

    fun packs(continent: Continent,archeageServer: ArcheageServer,pageable: Pageable) : Page<PackDTO>

    fun packs(destinationLocationID: Long,continent: Continent, departureLocationID: Long,  archeageServer: ArcheageServer,pageable: Pageable) : Page<PackDTO>

    fun packsCreatedAt(continent: Continent, departureLocationID: Long, archeageServer: ArcheageServer,pageable: Pageable) : Page<PackDTO>

    fun packsSoldAt(continent: Continent, destinationLocationID: Long, archeageServer: ArcheageServer,pageable: Pageable) : Page<PackDTO>


}