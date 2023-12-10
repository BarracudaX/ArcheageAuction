package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.repository.PackRecipeRepository
import com.arslan.archeage.repository.PackRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PackServiceImpl(private val packRepository: PackRepository,private val itemPriceService: ItemPriceService,private val packRecipeRepository: PackRecipeRepository) : PackService {

    override fun packs(continent: Continent,archeageServer: ArcheageServer): List<PackDTO> {
        return convertPacksToDTOs(packRepository.allPacks(archeageServer, continent),archeageServer)
    }

    override fun packs(continent: Continent, departureLocation: String, destinationLocation: String,archeageServer: ArcheageServer): List<PackDTO> {
        return convertPacksToDTOs(packRepository.packs(archeageServer, continent,departureLocation,destinationLocation),archeageServer)
    }

    override fun packsCreatedAt(continent: Continent, departureLocation: String,archeageServer: ArcheageServer): List<PackDTO> {
        return convertPacksToDTOs(packRepository.packsAt(archeageServer,continent,departureLocation),archeageServer)
    }

    override fun packsSoldAt(continent: Continent, destinationLocation: String,archeageServer: ArcheageServer): List<PackDTO> {
        return convertPacksToDTOs(packRepository.packsTo(archeageServer,continent,destinationLocation),archeageServer)
    }

    override fun purchasableCraftingMaterials(pageable: Pageable,archeageServer: ArcheageServer): Page<Item> = packRecipeRepository.findAllPurchasableCraftingMaterials(pageable,archeageServer.region)

    private fun convertPacksToDTOs(packs: List<Pack>,archeageServer: ArcheageServer) : List<PackDTO> {
        val userID = SecurityContextHolder.getContext()?.authentication?.name?.toLongOrNull()
        val materials = packs.flatMap { pack -> pack.recipes.flatMap { recipe -> recipe.materials.map(CraftingMaterial::item) } }
        val prices = if(userID == null){
            itemPriceService
                .latestPrices(materials,archeageServer)
                .associateBy { itemPrice -> itemPrice.purchasableItem.id!! }
        }else{
            itemPriceService.userPrices(materials,userID,archeageServer)
        }

        return packs.toDTO(prices).sortedByDescending(PackDTO::profit)
    }


}