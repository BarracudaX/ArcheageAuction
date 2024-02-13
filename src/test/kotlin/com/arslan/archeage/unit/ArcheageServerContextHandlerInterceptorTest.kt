package com.arslan.archeage.unit

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ArcheageServerContextHolder
import com.arslan.archeage.web.ArcheageServerContextHandlerInterceptor
import com.arslan.archeage.web.ArcheageServerContextHandlerInterceptor.Companion.MDC_ARCHEAGE_SERVER_KEY
import com.arslan.archeage.web.ArcheageServerResolver
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import org.testcontainers.shaded.org.apache.commons.lang3.arch.Processor.Arch

class ArcheageServerContextHandlerInterceptorTest {

    private val resolverMock = mockk<ArcheageServerResolver>()
    private val archeageServer = ArcheageServer("ANY",1)
    private val requestMock = mockk<HttpServletRequest>()
    private val responseMock = mockk<HttpServletResponse>()

    private val sut = ArcheageServerContextHandlerInterceptor(resolverMock)

    @BeforeEach
    fun setUp() {
        ArcheageServerContextHolder.clear()
        mockkStatic(MDC::class)
        every { MDC.put(MDC_ARCHEAGE_SERVER_KEY,any()) } just runs
        every { MDC.remove(MDC_ARCHEAGE_SERVER_KEY) } just runs
    }

    @Test
    fun `should retrieve and use the archeage server stored in context and store it in MDC in pre handle`() {
        ArcheageServerContextHolder.setServerContext(archeageServer)

        sut.preHandle(requestMock,responseMock,Any()) shouldBe true

        verifyAll {
            resolverMock wasNot called
            MDC.put(MDC_ARCHEAGE_SERVER_KEY,archeageServer.name)
        }

        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should retrieve and use the archeage server using resolver if context is empty and store it in MDC in pre handle`() {
        ArcheageServerContextHolder.clear()
        every { resolverMock.resolveArcheageServer(requestMock) } returns archeageServer

        sut.preHandle(requestMock,responseMock,Any()) shouldBe true

        verifyAll {
            resolverMock.resolveArcheageServer(requestMock)
            MDC.put(MDC_ARCHEAGE_SERVER_KEY,archeageServer.name)
        }
    }

    @Test
    fun `should return true and do nothing if context is empty and resolver returns null`() {
        ArcheageServerContextHolder.clear()
        every { MDC.put(any(),any()) } throws IllegalStateException("Call should not happen!")
        every { resolverMock.resolveArcheageServer(requestMock) } returns null

        sut.preHandle(requestMock,responseMock,Any()) shouldBe true

        verifyAll {
            resolverMock.resolveArcheageServer(requestMock)
        }
    }

    @Test
    fun `should remove archeage server from MDC and clear context in post handle`() {
        ArcheageServerContextHolder.setServerContext(ArcheageServer("ANY",1))
        ArcheageServerContextHolder.getServerContext() shouldNotBe null

        sut.postHandle(requestMock,responseMock,Any(),null)

        ArcheageServerContextHolder.getServerContext() shouldBe null
        verifyAll {
            MDC.remove(MDC_ARCHEAGE_SERVER_KEY)
        }
    }

    @Test
    fun `should remove archeage server from MDC and clear context after completion`() {
        ArcheageServerContextHolder.setServerContext(ArcheageServer("ANY",1))
        ArcheageServerContextHolder.getServerContext() shouldNotBe null

        sut.afterCompletion(requestMock,responseMock,Any(),null)

        ArcheageServerContextHolder.getServerContext() shouldBe null
        verifyAll {
            MDC.remove(MDC_ARCHEAGE_SERVER_KEY)
        }
    }
}