package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.item.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PackService {

    fun packs(continent: Continent,archeageServer: ArcheageServer) : List<PackDTO>

    fun packs(continent: Continent,departureLocation: String,destinationLocation: String,archeageServer: ArcheageServer) : List<PackDTO>

    fun packsCreatedAt(continent: Continent, departureLocation: String,archeageServer: ArcheageServer) : List<PackDTO>

    fun packsSoldAt(continent: Continent, destinationLocation: String,archeageServer: ArcheageServer) : List<PackDTO>

    fun purchasableCraftingMaterials(pageable: Pageable,archeageServer: ArcheageServer) : Page<Item>

}