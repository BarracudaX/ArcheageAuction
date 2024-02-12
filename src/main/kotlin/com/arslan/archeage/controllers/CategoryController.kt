package com.arslan.archeage.controllers

import com.arslan.archeage.CategoryDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.CategoryService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/category")
@RestController
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun categories(archeageServer: ArcheageServer) : ResponseEntity<List<CategoryDTO>>{
        return ResponseEntity.ok(categoryService.categories(archeageServer))
    }

}