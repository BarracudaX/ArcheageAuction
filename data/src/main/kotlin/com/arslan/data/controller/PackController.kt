package com.arslan.data.controller

import com.arslan.data.PackDTO
import com.arslan.data.entity.Continent
import com.arslan.data.entity.Pack
import com.arslan.data.service.PackService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/packs")
class PackController(private val packService: PackService) {

    @GetMapping("/location/{location}")
    fun packsAtLocation(@PathVariable location: String) : ResponseEntity<List<Pack>>{
        return ResponseEntity.ok(packService.packsAtLocation(location))
    }

    @GetMapping("/continent/{continent}")
    fun packAtContinent(@PathVariable continent: Continent) : ResponseEntity<List<PackDTO>> = ResponseEntity.ok(packService.packAtContinent(continent))
}