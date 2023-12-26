package com.arslan.archeage.controllers

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.Continent
import com.arslan.archeage.Packs
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.PackService
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.jvm.optionals.getOrDefault

@RequestMapping("/packs")
@RestController
class PackController(private val packService: PackService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun packs(@RequestParam continent: Optional<Continent>, @RequestParam(required = false) departureLocation: Long?, @RequestParam(required = false) destinationLocation: Long?, archeageServer: ArcheageServer?,pageable: Pageable) : ResponseEntity<Packs>{
        if(archeageServer == null){
            throw ArcheageContextHolderEmptyException()
        }
        val usedContinent = continent.getOrDefault(Continent.values()[0])

        val packs = if(departureLocation != null && destinationLocation != null){
            packService.packs(destinationLocation,usedContinent,departureLocation,archeageServer,pageable)
        }else if(departureLocation != null){
            packService.packsCreatedAt(usedContinent,departureLocation,archeageServer,pageable)
        }else if(destinationLocation != null){
            packService.packsSoldAt(usedContinent,destinationLocation,archeageServer,pageable)
        }else{
            packService.packs(usedContinent,archeageServer,pageable)
        }

        return ResponseEntity.ok(Packs(packs.content,packs.hasNext(),packs.hasPrevious()))
    }

}