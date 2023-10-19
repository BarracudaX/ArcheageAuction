package com.arslan.web.web

import com.arslan.web.service.ServerTranslator
import com.arslan.web.service.WebArcheageServerContextHolder
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor


/**
 * This interceptor changes the archeage server context and stores with the resolver if server parameter is present in the request.
 */
@Component
class WebArcheageServerChangeInterceptor(private val resolver: WebArcheageServerResolver, private val serverTranslator: ServerTranslator) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val server = request.getParameter("server")?.let {
            serverTranslator.translateToUseWithPriceService(it)
        }

        if(server != null){
            resolver.setArcheageServer(response,server)
            WebArcheageServerContextHolder.setServerContext(serverTranslator.translateToUseWithPriceService(server))
        }

        return true
    }

}