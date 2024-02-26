package com.arslan.archeage.service

import com.arslan.archeage.PackDTO
import com.arslan.archeage.PackRequest
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.repository.PackProfitRepository
import com.arslan.archeage.repository.PackRepository
import com.arslan.archeage.repository.PackResult
import com.arslan.archeage.toDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceService: ItemPriceService,private val packProfitRepository: PackProfitRepository) : PackService {

    override fun packs(packRequest: PackRequest,pageable: Pageable,archeageServer: ArcheageServer) :  Page<PackDTO> {
        return convertPacksToDTOs(packProfitRepository.packs(pageable,packRequest,archeageServer),packRequest)
    }

    override fun numOfPacks(): Long = packRepository.count()

    private fun convertPacksToDTOs(result: Page<PackResult>,request: PackRequest) : Page<PackDTO> {
        val percentages = result.content.associate { it.id to it.percentage }
        val packIDs = result.content.map(PackResult::id)

        val packs = packRepository.packs(packIDs)
            .map { it to packIDs.indexOf(it.id!!) }
            .sortedBy { (_,position) -> position } //packs can return packs in different order than the order of the initial result.
            .map { (pack,_) -> pack }

        val userID = SecurityContextHolder.getContext()?.authentication?.name?.toLongOrNull()
        val materials = packs.flatMap { pack -> pack.materials().map(CraftingMaterial::item) }
        val prices = if(userID == null){
            itemPriceService
                .lastPrices(materials.filterIsInstance<PurchasableItem>())
                .associateBy { itemPrice -> itemPrice.id.purchasableItem.id!! }
        }else{
            itemPriceService.userItemPrices(materials.filterIsInstance<PurchasableItem>(),userID)
        }

        return PageImpl(packs.toDTO(prices,percentages,request.userID != null),result.pageable,result.totalElements)
    }

}