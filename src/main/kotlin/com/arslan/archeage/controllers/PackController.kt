package com.arslan.archeage.controllers

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.Continent
import com.arslan.archeage.PackRequest
import com.arslan.archeage.Packs
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.PackService
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.jvm.optionals.getOrDefault

@RequestMapping("/packs")
@RestController
class PackController(private val packService: PackService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun packs(@ModelAttribute packRequest: PackRequest, archeageServer: ArcheageServer?,pageable: Pageable) : ResponseEntity<Packs>{
        if(archeageServer == null){
            throw ArcheageContextHolderEmptyException()
        }

        val packs = packService.packs(packRequest.copy(userID = SecurityContextHolder.getContext().authentication?.name?.toLongOrNull()),pageable,archeageServer)

        return ResponseEntity.ok(Packs(packs.content,packs.hasNext(),packs.hasPrevious()))
    }

}