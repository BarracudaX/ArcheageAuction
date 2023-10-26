package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.PackPrice
import com.arslan.archeage.repository.ItemPriceRepository
import com.arslan.archeage.repository.PackRepository
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceRepository: ItemPriceRepository) : PackService {

    override fun packs(continent: Continent): List<PackDTO>{
        if(ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        val packs = packRepository.packs(server.region,server)

        val latestItemPrices = itemPriceRepository.latestPrices(packs.flatMap { pack -> pack.recipes.flatMap { recipe -> recipe.materials }.map{ material -> material.item.id!! } }).associateBy { it.item.name }

        return packs.flatMap { pack ->
            pack.prices.filterIsInstance<PackPrice>().map { packPrice -> PackDTO(pack.name,pack.creationLocation.name,packPrice.sellLocation.name,packPrice.price,pack.recipes.map { recipe -> recipe.toDTO(latestItemPrices) }) }
        }
    }

}