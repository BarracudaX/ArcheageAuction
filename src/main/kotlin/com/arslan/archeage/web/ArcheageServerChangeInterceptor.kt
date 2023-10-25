package com.arslan.archeage.web

import com.arslan.archeage.entity.Region
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.service.ArcheageServerContextHolder
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import kotlin.jvm.optionals.getOrNull


/**
 * This interceptor changes the archeage server context and stores with the resolver if server parameter is present in the request.
 */
@Component
class ArcheageServerChangeInterceptor(private val resolver: ArcheageServerResolver,private val archeageServerRepository: ArcheageServerRepository) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val server = request.getParameter("server")?.let { archeageServerRepository.findById(it.toLong()).getOrNull() }

        if(server != null){
            resolver.setArcheageServer(response,server)
            ArcheageServerContextHolder.setServerContext(server)
        }

        return true
    }

}