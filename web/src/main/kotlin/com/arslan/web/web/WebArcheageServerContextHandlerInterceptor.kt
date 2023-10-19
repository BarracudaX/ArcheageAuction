package com.arslan.web.web

import com.arslan.web.service.ServerService
import com.arslan.web.service.ServerTranslator
import com.arslan.web.service.WebArcheageServerContextHolder
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception

/**
 * This interceptor saves the currently chosen archeage server in web exchange and MDC.
 * Also, if locale change happens, this interceptor changes the archeage server to a default server of that locale(which is the first server returned from server service).
 */
@Component
class WebArcheageServerContextHandlerInterceptor(private val resolver: WebArcheageServerResolver, private val serverService: ServerService,private val serverTranslator: ServerTranslator) : HandlerInterceptor{

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        var currentServer = WebArcheageServerContextHolder.getServerContext() ?: resolver.resolveArcheageServer(request)
        val availableServers = serverService.servers().map { serverTranslator.translateToUseWithPriceService(it) }

        if(!availableServers.contains(currentServer)){
            resolver.setArcheageServer(response,availableServers[0])
            currentServer = availableServers[0]
        }

        WebArcheageServerContextHolder.setServerContext(currentServer!!)
        MDC.put("archeage-server",currentServer)

        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        if(modelAndView != null){
            modelAndView.model["server"] = serverTranslator.translateToUseWithView(WebArcheageServerContextHolder.getServerContext()!!)
        }
        MDC.remove("archeage-server")
        WebArcheageServerContextHolder.clear()
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        MDC.remove("archeage-server")
        WebArcheageServerContextHolder.clear()
    }

}