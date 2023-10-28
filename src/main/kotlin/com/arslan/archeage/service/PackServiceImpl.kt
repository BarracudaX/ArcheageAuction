package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Pack
import com.arslan.archeage.repository.PackRepository
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository) : PackService {

    override fun packs(continent: Continent): List<Pack> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return packRepository.allPacks(server.region, server, continent)
    }

    override fun packs(continent: Continent, departureLocation: String, destinationLocation: String): List<Pack> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return packRepository.packs(server.region, server, continent,departureLocation,destinationLocation)
    }

    override fun packsAt(continent: Continent, departureLocation: String): List<Pack> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return packRepository.packsAt(server.region,server,continent,departureLocation)
    }

    override fun packsTo(continent: Continent, destinationLocation: String): List<Pack> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return packRepository.packsTo(server.region,server,continent,destinationLocation)
    }


}