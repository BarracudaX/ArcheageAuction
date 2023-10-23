package com.arslan.web.controllers

import com.arslan.web.service.ServerService
import com.arslan.web.service.WebArcheageServerContextHolder
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import java.util.concurrent.ConcurrentHashMap

@ControllerAdvice
class WebControllerAdvice(private val serverService: ServerService) {
    @ModelAttribute("servers")
    fun servers() : List<String> = serverService.servers()

    @ModelAttribute("currentURL")
    fun currentURL(httpServletRequest: HttpServletRequest) : String = httpServletRequest.requestURL.toString()

}