package com.arslan.archeage.controllers

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.PackPercentageUpdate
import com.arslan.archeage.PackRequest
import com.arslan.archeage.Packs
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.PackProfitService
import com.arslan.archeage.service.PackService
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/pack")
@RestController
class PackController(private val packService: PackService,private val packProfitService: PackProfitService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun packs(@ModelAttribute packRequest: PackRequest, archeageServer: ArcheageServer,pageable: Pageable) : ResponseEntity<Packs>{
        val userID = SecurityContextHolder.getContext().authentication?.name?.toLongOrNull()

        val packs = packService.packs(packRequest.copy(userID = userID, categories = packRequest.categories?.let { it.ifEmpty { null } }),pageable,archeageServer)

        return ResponseEntity.ok(Packs(packs.content,packs.hasNext(),packs.hasPrevious(),userID != null))
    }

    @PutMapping("/percentage", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateProfit(@RequestBody percentageUpdate: PackPercentageUpdate) : ResponseEntity<Unit>{
        val userID = SecurityContextHolder.getContext().authentication?.name?.toLong()!!

        packProfitService.updatePercentage(percentageUpdate.copy(userID = userID))

        return ResponseEntity.ok(Unit)
    }

}