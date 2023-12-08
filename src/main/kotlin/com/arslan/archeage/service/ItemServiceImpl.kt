package com.arslan.archeage.service

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.repository.ItemRecipeRepository
import com.arslan.archeage.repository.PackRecipeRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemServiceImpl(private val packRecipeRepository: PackRecipeRepository) : ItemService {


    override fun packCraftingMaterials(pageable: Pageable): Page<Item> = packRecipeRepository.findAllCraftingMaterials(pageable)

}