package com.arslan.archeage.web

import com.arslan.archeage.service.ArcheageServerContextHolder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception

/**
 * This interceptor saves the currently chosen archeage server in web exchange and MDC.
 */
@Component
class ArcheageServerContextHandlerInterceptor(private val resolver: ArcheageServerResolver) : HandlerInterceptor{

    companion object{
        const val MDC_ARCHEAGE_SERVER_KEY = "archeage-server"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        //first check in context,and only after use the resolver. Because if the user changed the server in the current request, the resolver will return the previous server value from the request.
        val currentServer = ArcheageServerContextHolder.getServerContext() ?: resolver.resolveArcheageServer(request)

        if(currentServer != null){
            ArcheageServerContextHolder.setServerContext(currentServer)
            MDC.put(MDC_ARCHEAGE_SERVER_KEY, currentServer.name)
        }

        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        MDC.remove(MDC_ARCHEAGE_SERVER_KEY)
        ArcheageServerContextHolder.clear()
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        MDC.remove(MDC_ARCHEAGE_SERVER_KEY)
        ArcheageServerContextHolder.clear()
    }

}