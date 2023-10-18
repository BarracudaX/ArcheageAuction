package com.arslan.data.controller

import com.arslan.data.entity.Item
import com.arslan.data.service.ItemService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/items")
@RestController
class ItemController(private val itemService: ItemService) {

    @GetMapping
    fun items() : ResponseEntity<List<Item>> = ResponseEntity.ok(itemService.items())

}