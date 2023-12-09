package com.arslan.archeage.controllers

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.UserPrices
import com.arslan.archeage.service.ArcheageServerContextHolder
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.PackService
import com.arslan.archeage.toDTO
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user/price")
@RestController
class UserPriceController(private val packService: PackService,private val itemPriceService: ItemPriceService) {

    @GetMapping
    fun prices(pageable: Pageable): ResponseEntity<UserPrices> {
        val userID = SecurityContextHolder.getContext().authentication.name.toLong()
        val archeageServer = ArcheageServerContextHolder.getServerContext() ?: throw ArcheageContextHolderEmptyException()

        val items = packService.purchasableCraftingMaterials(pageable,archeageServer)
        val itemsDTO = PageImpl(items.content.map { it.toDTO() },pageable,items.totalElements)
        val prices = itemPriceService.userPrices(items.content,userID).mapValues { (_,value) -> value.price }

        return ResponseEntity.ok(UserPrices(itemsDTO,prices,items.hasNext(),items.hasPrevious()))
    }

}