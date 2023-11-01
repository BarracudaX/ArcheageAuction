package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.*
import com.arslan.archeage.repository.PackRepository
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceService: ItemPriceService) : PackService {

    override fun packs(continent: Continent): List<PackDTO> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return convertPacksToDTOs(packRepository.allPacks(server, continent))
    }

    override fun packs(continent: Continent, departureLocation: String, destinationLocation: String): List<PackDTO> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return convertPacksToDTOs(packRepository.packs(server, continent,departureLocation,destinationLocation))
    }

    override fun packsCreatedAt(continent: Continent, departureLocation: String): List<PackDTO> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return convertPacksToDTOs(packRepository.packsAt(server,continent,departureLocation))
    }

    override fun packsSoldAt(continent: Continent, destinationLocation: String): List<PackDTO> {
        if (ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        val server = ArcheageServerContextHolder.getServerContext()!!

        return convertPacksToDTOs(packRepository.packsTo(server,continent,destinationLocation))
    }

    private fun convertPacksToDTOs(packs: List<Pack>) : List<PackDTO> {
        val prices = itemPriceService
            .latestPrices(packs.flatMap { pack -> pack.recipes.flatMap { recipe -> recipe.materials.map(CraftingMaterial::item) } })
            .associateBy { itemPrice -> itemPrice.item.name }

        return packs.toDTO(prices).sortedByDescending(PackDTO::profit)
    }


}