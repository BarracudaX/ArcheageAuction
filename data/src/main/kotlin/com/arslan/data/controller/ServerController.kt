package com.arslan.data.controller

import com.arslan.data.entity.Server
import com.arslan.data.service.ServerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/servers")
@RestController
class ServerController(private val serverService: ServerService) {

    @GetMapping
    fun servers() : ResponseEntity<List<String>> = ResponseEntity.ok(serverService.servers().map(Server::name))

}