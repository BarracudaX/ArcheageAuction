package com.arslan.archeage.controllers

import com.arslan.archeage.*
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ItemPriceService
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/user/price")
@RestController
class UserPriceController(private val itemPriceService: ItemPriceService,private val messageSource: MessageSource) {

    @GetMapping
    fun prices(pageable: Pageable,archeageServer: ArcheageServer?): ResponseEntity<UserPrices> {
        if(archeageServer == null) throw ArcheageContextHolderEmptyException()

        val userID = SecurityContextHolder.getContext().authentication.name.toLong()

        val prices = itemPriceService.userPrices(userID,pageable)

        return ResponseEntity.ok(UserPrices(prices.content.map { ItemDTO(it.id.purchasableItem.name,it.id.purchasableItem.id!!,it.price) },prices.hasNext(),prices.hasPrevious()))
    }

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun userPrice(@RequestBody price: UserPriceDTO) : ResponseEntity<String>{
        val userID = SecurityContextHolder.getContext().authentication.name.toLong()

        itemPriceService.saveUserPrice(price.copy(userID = userID))

        return ResponseEntity.ok(messageSource.getMessage("price.save.success", emptyArray(),LocaleContextHolder.getLocale()))
    }

}