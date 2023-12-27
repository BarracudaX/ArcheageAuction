package com.arslan.archeage.service

import com.arslan.archeage.PackDTO
import com.arslan.archeage.PackRequest
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.repository.PackProfitRepository
import com.arslan.archeage.repository.PackRepository
import com.arslan.archeage.toDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceService: ItemPriceService,private val packProfitRepository: PackProfitRepository) : PackService {

    override fun packs(packRequest: PackRequest,pageable: Pageable,archeageServer: ArcheageServer) :  Page<PackDTO> {
        return with(packRequest){
            if(departureLocation != null && destinationLocation != null){
                convertPacksToDTOs(packProfitRepository.packIDs(pageable,packRequest,archeageServer))
            }else if(departureLocation != null){
                convertPacksToDTOs(packProfitRepository.packsAtIDs(pageable,packRequest,archeageServer))
            }else if(destinationLocation != null){
                convertPacksToDTOs(packProfitRepository.packsToIDs(pageable,packRequest,archeageServer))
            }else{
                convertPacksToDTOs(packProfitRepository.allPackIDs(pageable,packRequest,archeageServer))
            }
        }
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