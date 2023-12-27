package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.PackRequest
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.item.Item
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PackService {
    fun packs(packRequest: PackRequest,pageable: Pageable,archeageServer: ArcheageServer) : Page<PackDTO>

}