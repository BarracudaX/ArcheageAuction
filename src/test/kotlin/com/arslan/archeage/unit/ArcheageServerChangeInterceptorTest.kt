package com.arslan.archeage.unit

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.service.ArcheageServerContextHolder
import com.arslan.archeage.web.ArcheageServerChangeInterceptor
import com.arslan.archeage.web.ArcheageServerChangeInterceptor.Companion.ARCHEAGE_SERVER_PARAMETER_NAME
import com.arslan.archeage.web.ArcheageServerResolver
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ArcheageServerChangeInterceptorTest {

    private val archeageServerResolverMock = mockk<ArcheageServerResolver>()
    private val archeageServerRepositoryMock = mockk<ArcheageServerRepository>()
    private val requestMock = mockk<HttpServletRequest>()
    private val responseMock = mockk<HttpServletResponse>()
    private val sut = ArcheageServerChangeInterceptor(archeageServerResolverMock,archeageServerRepositoryMock)

    @BeforeEach
    fun setUp() {
        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should return true and not change archeage server if request does not contain server parameter`() {
        every { requestMock.getParameter(ARCHEAGE_SERVER_PARAMETER_NAME) } returns null

        sut.preHandle(requestMock,responseMock,Any()) shouldBe true

        ArcheageServerContextHolder.getServerContext() shouldBe null
        verifyAll {
            archeageServerResolverMock wasNot called
            archeageServerRepositoryMock wasNot called
        }
    }

    @Test
    fun `should throw NumberFormatException if request contains server parameter that has invalid value`() {
        every { requestMock.getParameter(ARCHEAGE_SERVER_PARAMETER_NAME) } returns "NOT_A_NUMBER"

        shouldThrow<NumberFormatException> { sut.preHandle(requestMock,responseMock,Any()) }

        ArcheageServerContextHolder.getServerContext() shouldBe null
        verifyAll {
            archeageServerResolverMock wasNot called
            archeageServerRepositoryMock wasNot called
        }
    }

    @Test
    fun `should return true change archeage server using resolver and store new archeage server in the context`() {
        val expectedArcheageServer = ArcheageServer("ANY",1)
        every { requestMock.getParameter(ARCHEAGE_SERVER_PARAMETER_NAME) } returns expectedArcheageServer.id.toString()
        every { archeageServerRepositoryMock.findById(expectedArcheageServer.id!!) } returns Optional.of(expectedArcheageServer)
        every { archeageServerResolverMock.setArcheageServer(responseMock,expectedArcheageServer) } just runs

        sut.preHandle(requestMock,responseMock,Any())

        ArcheageServerContextHolder.getServerContext() shouldNotBe null shouldBe expectedArcheageServer
        verifyAll {
            archeageServerResolverMock.setArcheageServer(responseMock,expectedArcheageServer)
            archeageServerRepositoryMock.findById(expectedArcheageServer.id!!)
        }
    }

    @Test
    fun `should return true and not change archeage server if request contains server parameter but repository does not find it`() {
        every { requestMock.getParameter(ARCHEAGE_SERVER_PARAMETER_NAME) } returns "1"
        every { archeageServerRepositoryMock.findById(1) } returns Optional.empty()

        sut.preHandle(requestMock,responseMock,Any())

        ArcheageServerContextHolder.getServerContext() shouldBe null
        verifyAll {
            archeageServerRepositoryMock.findById(1)
            archeageServerResolverMock wasNot called
        }
    }
}