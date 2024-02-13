package com.arslan.archeage.web

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.repository.ArcheageServerRepository
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

    companion object{
        @JvmStatic
        val SERVER_COOKIE = ResponseCookie.from(SERVER_COOKIE_NAME).path("/").sameSite("Lax").build()
    }

    override fun resolveArcheageServer(request: HttpServletRequest): ArcheageServer? {
        val serverCookie = WebUtils.getCookie(request, SERVER_COOKIE_NAME)?.value?.toLongOrNull() ?: return null

        return archeageServerRepository.findById(serverCookie).getOrNull()
    }

    override fun setArcheageServer(response : HttpServletResponse, server: ArcheageServer) {
        response.addHeader(HttpHeaders.SET_COOKIE,SERVER_COOKIE.mutate().value("${server.id}").build().toString())
    }
}