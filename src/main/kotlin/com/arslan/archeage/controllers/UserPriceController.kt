package com.arslan.archeage.controllers

import com.arslan.archeage.UserPrices
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.ItemService
import com.arslan.archeage.toDTO
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user/price")
@RestController
class UserPriceController(private val itemService: ItemService,private val itemPriceService: ItemPriceService) {

    @GetMapping
    fun prices(pageable: Pageable): UserPrices {
        val userID = SecurityContextHolder.getContext().authentication.name.toLong()
        val items = itemService.packCraftingMaterials(pageable)
        val itemsDTO = PageImpl(items.content.map { it.toDTO() },pageable,items.totalElements)
        val prices = itemPriceService.userPrices(items.content,userID).mapValues { (_,value) -> value.price }

        return UserPrices(itemsDTO,prices,items.hasNext(),items.hasPrevious())
    }

}