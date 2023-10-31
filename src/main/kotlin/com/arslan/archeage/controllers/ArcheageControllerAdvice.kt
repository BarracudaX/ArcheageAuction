package com.arslan.archeage.controllers

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ArcheageServerContextHolder
import com.arslan.archeage.service.ArcheageServerService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import java.util.TimeZone

@ControllerAdvice
class ArcheageControllerAdvice(private val archeageServerService: ArcheageServerService) {
    @ModelAttribute("servers")
    fun servers() : List<ArcheageServer?> = archeageServerService.servers().flatMap { (_,servers) -> servers.plus(null) } // null is used as group separator.

    @ModelAttribute("server")
    fun server() : ArcheageServer? = ArcheageServerContextHolder.getServerContext()

    @ModelAttribute("currentURL")
    fun currentURL(httpServletRequest: HttpServletRequest) : String = httpServletRequest.requestURL.toString()

    @ModelAttribute("timezone")
    fun timezone() : TimeZone = LocaleContextHolder.getTimeZone()
}