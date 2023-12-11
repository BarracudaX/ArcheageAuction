package com.arslan.archeage.controllers

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ArcheageServerContextHolder
import com.arslan.archeage.service.ArcheageServerService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ModelAttribute
import java.util.TimeZone

@ControllerAdvice
class ArcheageControllerAdvice(private val archeageServerService: ArcheageServerService,private val messageSource: MessageSource) {
    @ModelAttribute("servers")
    fun servers() : List<ArcheageServer?> = archeageServerService.servers()

    @ModelAttribute("server")
    fun server() : ArcheageServer? = ArcheageServerContextHolder.getServerContext()

    @ModelAttribute("currentURL")
    fun currentURL(httpServletRequest: HttpServletRequest) : String = httpServletRequest.requestURL.toString()

    @ModelAttribute("timezone")
    fun timezone() : TimeZone = LocaleContextHolder.getTimeZone()

    @ExceptionHandler(ArcheageContextHolderEmptyException::class)
    fun archeageContextHolderEmptyHandler() : ResponseEntity<String> =
        ResponseEntity.badRequest().body(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),LocaleContextHolder.getLocale()))
}