package com.arslan.price.controller

import com.arslan.price.entity.ItemPrice
import com.arslan.price.service.ItemService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/item")
@RestController
class ItemController(private val itemService: ItemService) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun lastPrices(@RequestBody items: Collection<String>) : ResponseEntity<List<ItemPrice>> = ResponseEntity.ok(itemService.latestPrices(items))

}