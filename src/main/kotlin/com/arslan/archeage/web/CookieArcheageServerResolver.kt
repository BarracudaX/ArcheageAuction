package com.arslan.archeage.web

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Region
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.service.ArcheageServerContextHolder.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import kotlin.jvm.optionals.getOrNull

const val SERVER_COOKIE_NAME = "ARCHEAGE_SERVER"

@Component
class CookieArcheageServerResolver(private val archeageServerRepository: ArcheageServerRepository) : ArcheageServerResolver {

    private val serverCookie = ResponseCookie.from(SERVER_COOKIE_NAME).path("/").sameSite("Lax").build()

    override fun resolveArcheageServer(request: HttpServletRequest): ArcheageServer? {
        val serverCookie = WebUtils.getCookie(request, SERVER_COOKIE_NAME)?.value?.toLongOrNull() ?: return null

        return archeageServerRepository.findById(serverCookie).getOrNull()
    }

    override fun setArcheageServer(response : HttpServletResponse, server: ArcheageServer) {
        response.addHeader(HttpHeaders.SET_COOKIE,serverCookie.mutate().value("${server.id}").build().toString())
    }
}