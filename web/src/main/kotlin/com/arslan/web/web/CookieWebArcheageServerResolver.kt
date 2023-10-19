package com.arslan.web.web

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils

const val SERVER_COOKIE_NAME = "ARCHEAGE_SERVER"

@Component
class CookieWebArcheageServerResolver : WebArcheageServerResolver {

    private val cookie = ResponseCookie.from(SERVER_COOKIE_NAME).path("/").sameSite("Lax").build()

    override fun resolveArcheageServer(request: HttpServletRequest) = WebUtils.getCookie(request, SERVER_COOKIE_NAME)?.value

    override fun setArcheageServer(response : HttpServletResponse,server: String) {
        response.addHeader(HttpHeaders.SET_COOKIE,cookie.mutate().value(server).build().toString())
    }
}