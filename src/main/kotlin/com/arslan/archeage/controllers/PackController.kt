package com.arslan.archeage.controllers

import com.arslan.archeage.*
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.PackProfitService
import com.arslan.archeage.service.PackService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*

@RequestMapping("/pack")
@RestController
class PackController(private val packService: PackService,private val packProfitService: PackProfitService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun packs(@RequestParam params: MultiValueMap<String,String>, archeageServer: ArcheageServer,@AuthenticationPrincipal authentication: Long?) : ResponseEntity<PacksDataTableResponse>{
        val packRequest = params.toPacksRequest(authentication)
        val pageable = params.packPageable()
        return ResponseEntity.ok(packService.packs(packRequest,pageable,archeageServer).toDataTableResponse(params,packService.numOfPacks()))
    }

    @PutMapping("/percentage", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateProfit(@RequestBody percentageUpdate: PackPercentageUpdate,@AuthenticationPrincipal authentication: Long) : ResponseEntity<Unit>{
        packProfitService.updatePercentage(percentageUpdate.copy(userID = authentication))

        return ResponseEntity.ok(Unit)
    }

}