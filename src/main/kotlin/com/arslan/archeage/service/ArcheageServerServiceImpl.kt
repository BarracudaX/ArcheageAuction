package com.arslan.archeage.service

import com.arslan.archeage.entity.Region
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.repository.ArcheageServerRepository
import org.springframework.stereotype.Service


@Service
class ArcheageServerServiceImpl(private val archeageServerRepository: ArcheageServerRepository) : ArcheageServerService {

    override fun servers(): Map<Region, List<ArcheageServer>> = archeageServerRepository.findAll().groupBy(ArcheageServer::region)

}