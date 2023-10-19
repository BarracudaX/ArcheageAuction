package com.arslan.web.web

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface WebArcheageServerResolver {

    fun resolveArcheageServer(request: HttpServletRequest) : String?

    fun setArcheageServer(response : HttpServletResponse,server: String)
}