package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.item.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PackService {

    fun packs(continent: Continent,archeageServer: ArcheageServer) : List<PackDTO>

    fun packs(continent: Continent, departureLocationID: Long, destinationLocationID: Long, archeageServer: ArcheageServer) : List<PackDTO>

    fun packsCreatedAt(continent: Continent, departureLocationID: Long, archeageServer: ArcheageServer) : List<PackDTO>

    fun packsSoldAt(continent: Continent, destinationLocationID: Long, archeageServer: ArcheageServer) : List<PackDTO>

    fun purchasableCraftingMaterials(pageable: Pageable,archeageServer: ArcheageServer) : Page<Item>

}