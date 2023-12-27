package com.arslan.archeage.web

import com.arslan.archeage.entity.ArcheageServer
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface ArcheageServerResolver {

    fun resolveArcheageServer(request: HttpServletRequest) : ArcheageServer?

    fun setArcheageServer(response : HttpServletResponse, server: ArcheageServer)
}