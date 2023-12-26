package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.repository.PackProfitRepository
import com.arslan.archeage.repository.PackRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceService: ItemPriceService,private val packProfitRepository: PackProfitRepository) : PackService {

    override fun packs(continent: Continent,archeageServer: ArcheageServer,pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packProfitRepository.allPackIDs(pageable,archeageServer, continent,SecurityContextHolder.getContext().authentication?.name?.toLongOrNull()))
    }

    override fun packs(destinationLocationID: Long, continent: Continent, departureLocationID: Long, archeageServer: ArcheageServer, pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packProfitRepository.packIDs(pageable,archeageServer, continent,departureLocationID,destinationLocationID,SecurityContextHolder.getContext().authentication?.name?.toLongOrNull()))
    }

    override fun packsCreatedAt(continent: Continent, departureLocationID: Long, archeageServer: ArcheageServer,pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packProfitRepository.packsAtIDs(pageable,archeageServer,continent,departureLocationID,SecurityContextHolder.getContext().authentication?.name?.toLongOrNull()))
    }

    override fun packsSoldAt(continent: Continent, destinationLocationID: Long, archeageServer: ArcheageServer,pageable: Pageable): Page<PackDTO> {
        return convertPacksToDTOs(packProfitRepository.packsToIDs(pageable,archeageServer,continent,destinationLocationID,SecurityContextHolder.getContext().authentication?.name?.toLongOrNull()))
    }

    private fun convertPacksToDTOs(packsIDs: Page<Long>) : Page<PackDTO> {
        val packs = packRepository.packs(packsIDs.content)
        val userID = SecurityContextHolder.getContext()?.authentication?.name?.toLongOrNull()
        val materials = packs.flatMap { pack -> pack.materials().map(CraftingMaterial::item) }
        val prices = if(userID == null){
            itemPriceService
                .prices(materials.filterIsInstance<PurchasableItem>())
                .associateBy { itemPrice -> itemPrice.id.purchasableItem.id!! }
        }else{
            itemPriceService.userItemPrices(materials.filterIsInstance<PurchasableItem>(),userID)
        }

        return PageImpl(packs.toDTO(prices),packsIDs.pageable,packsIDs.totalElements)
    }


}