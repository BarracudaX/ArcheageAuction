package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.repository.ArcheageServerRepository
import org.springframework.stereotype.Service


@Service
class ArcheageServerServiceImpl(private val archeageServerRepository: ArcheageServerRepository) : ArcheageServerService {

    override fun servers(): List<ArcheageServer> = archeageServerRepository.findAll()

}