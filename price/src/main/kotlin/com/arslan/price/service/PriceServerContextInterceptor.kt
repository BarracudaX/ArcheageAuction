package com.arslan.price.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception

const val HTTP_HEADER_NAME = "X-ARCHEAGE-SERVER"

@Component
class PriceServerContextInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        ArcheageServerContextHolder.setServerContext(request.getHeader(HTTP_HEADER_NAME))

        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        ArcheageServerContextHolder.clear()
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        ArcheageServerContextHolder.clear()
    }
}