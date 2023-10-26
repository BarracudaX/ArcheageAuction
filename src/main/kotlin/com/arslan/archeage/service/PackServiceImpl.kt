package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.Pack
import com.arslan.archeage.repository.ItemPriceRepository
import com.arslan.archeage.repository.PackRepository
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceRepository: ItemPriceRepository) : PackService {

    override fun packs(continent: Continent): List<Pack> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return packRepository.packs(server.region, server, continent)
    }

}