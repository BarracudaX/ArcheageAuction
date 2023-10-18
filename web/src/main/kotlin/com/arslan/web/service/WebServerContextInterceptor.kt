package com.arslan.web.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception


const val HTTP_HEADER_NAME = "X-ARCHEAGE-SERVER"

@Component
class WebServerContextInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val server = request.getHeader(HTTP_HEADER_NAME)
        WebArcheageServerContextHolder.setServerContext(server)
        MDC.put("archeage-server",server)
        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        WebArcheageServerContextHolder.clear()
        MDC.remove("archeage-server")
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        WebArcheageServerContextHolder.clear()
        MDC.remove("archeage-server")
    }
}