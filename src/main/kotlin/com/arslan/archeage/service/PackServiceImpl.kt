package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.repository.PackRepository
import org.springframework.security.core.context.SecurityContextHolder
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
        val userID = SecurityContextHolder.getContext()?.authentication?.name?.toLongOrNull()
        val materials = packs.flatMap { pack -> pack.recipes.flatMap { recipe -> recipe.materials.map(CraftingMaterial::item) } }
        val prices = if(userID == null){
            itemPriceService
                .latestPrices(materials)
                .associateBy { itemPrice -> itemPrice.purchasableItem.id!! }
        }else{
            itemPriceService.userPrices(materials,userID)
        }

        return packs.toDTO(prices).sortedByDescending(PackDTO::profit)
    }


}