package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.repository.PackRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceService: ItemPriceService) : PackService {

    override fun packs(continent: Continent,archeageServer: ArcheageServer,pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packRepository.allPacksIDs(pageable,archeageServer, continent),archeageServer)
    }

    override fun packs(destinationLocationID: Long, continent: Continent, departureLocationID: Long, archeageServer: ArcheageServer, pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packRepository.packsIDS(pageable,archeageServer, continent,departureLocationID,destinationLocationID),archeageServer)
    }

    override fun packsCreatedAt(continent: Continent, departureLocationID: Long, archeageServer: ArcheageServer,pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packRepository.packsAtIDs(pageable,archeageServer,continent,departureLocationID),archeageServer)
    }

    override fun packsSoldAt(continent: Continent, destinationLocationID: Long, archeageServer: ArcheageServer,pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packRepository.packsToIDs(pageable,archeageServer,continent,destinationLocationID),archeageServer)
    }

    private fun convertPacksToDTOs(packsIDs: Page<Long>,archeageServer: ArcheageServer) : Page<PackDTO> {
        val packs = packRepository.packs(packsIDs.content)
        val userID = SecurityContextHolder.getContext()?.authentication?.name?.toLongOrNull()
        val materials = packs.flatMap { pack -> pack.materials().map(CraftingMaterial::item) }
        val prices = if(userID == null){
            itemPriceService
                .latestPrices(materials,archeageServer)
                .associateBy { itemPrice -> itemPrice.purchasableItem.id!! }
        }else{
            itemPriceService.userPrices(materials,userID,archeageServer)
        }

        return PageImpl(packs.toDTO(prices).sortedByDescending(PackDTO::profit),packsIDs.pageable,packsIDs.totalElements)
    }


}