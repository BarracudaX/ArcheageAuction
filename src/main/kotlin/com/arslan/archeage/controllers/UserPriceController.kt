package com.arslan.archeage.controllers

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.UserPrices
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.ItemService
import com.arslan.archeage.service.PackService
import com.arslan.archeage.toDTO
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user/price")
@RestController
class UserPriceController(private val itemPriceService: ItemPriceService,private val itemService: ItemService,private val messageSource: MessageSource) {

    @GetMapping
    fun prices(pageable: Pageable,archeageServer: ArcheageServer?): ResponseEntity<UserPrices> {
        if(archeageServer == null) throw ArcheageContextHolderEmptyException()

        val userID = SecurityContextHolder.getContext().authentication.name.toLong()

        val items = itemService.purchasableItems(pageable,archeageServer)
        val prices = itemPriceService.userPrices(items.content,userID).mapValues { (_,value) -> value.price }
        val itemsDTO = PageImpl(items.content.map { it.toDTO(prices[it.id!!]) },pageable,items.totalElements)

        return ResponseEntity.ok(UserPrices(itemsDTO,items.hasNext(),items.hasPrevious()))
    }

    @PostMapping
    fun userPrice(@RequestBody price: UserPriceDTO) : ResponseEntity<String>{
        val userID = SecurityContextHolder.getContext().authentication.name.toLong()

        itemPriceService.saveUserPrice(price.copy(userID = userID))

        return ResponseEntity.ok(messageSource.getMessage("price.save.success", emptyArray(),LocaleContextHolder.getLocale()))
    }

}