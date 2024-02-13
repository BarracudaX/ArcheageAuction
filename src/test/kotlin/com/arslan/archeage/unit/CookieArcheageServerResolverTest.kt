package com.arslan.archeage.unit

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.web.CookieArcheageServerResolver
import com.arslan.archeage.web.CookieArcheageServerResolver.Companion.SERVER_COOKIE
import com.arslan.archeage.web.SERVER_COOKIE_NAME
import io.kotest.matchers.shouldBe
import io.mockk.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpHeaders
import java.util.*

class CookieArcheageServerResolverTest {

    private val repositoryMock = mockk<ArcheageServerRepository>()
    private val requestMock = mockk<HttpServletRequest>()
    private val responseMock = mockk<HttpServletResponse>()

    private val sut = CookieArcheageServerResolver(repositoryMock)

    companion object{

        @JvmStatic
        fun invalidArcheageServerValues() : Array<String> = arrayOf("NOT_A_NUMBER","  ","","!")

    }

    @Test
    fun `should return null when trying to resolve archeage server and request does not contain archeage server cookie`() {
        every { requestMock.cookies } returns emptyArray()

        sut.resolveArcheageServer(requestMock) shouldBe null

        verifyAll { repositoryMock wasNot called }
    }

    @MethodSource("invalidArcheageServerValues")
    @ParameterizedTest
    fun `should return null when trying to resolve archeage server and request contains invalid archeage server value`(invalidArcheageServerValue: String) {
        every { requestMock.cookies } returns arrayOf(Cookie(SERVER_COOKIE_NAME,invalidArcheageServerValue))

        sut.resolveArcheageServer(requestMock) shouldBe null

        verifyAll { repositoryMock wasNot called }
    }

    @Test
    fun `should return null when trying to resolve archeage server and request contains archeage server id for which repository returns empty optional`() {
        val archeageServerID = 1L
        every { requestMock.cookies } returns arrayOf(Cookie(SERVER_COOKIE_NAME,"$archeageServerID"))
        every { repositoryMock.findById(archeageServerID) } returns Optional.empty()

        sut.resolveArcheageServer(requestMock) shouldBe null

        verifyAll {
            repositoryMock.findById(archeageServerID)
        }
    }

    @Test
    fun `should return archeage server resolved from request`() {
        val archeageServer = ArcheageServer("ANY",1)
        every { requestMock.cookies } returns arrayOf(Cookie(SERVER_COOKIE_NAME,"${archeageServer.id}"))
        every { repositoryMock.findById(archeageServer.id!!) } returns Optional.of(archeageServer)

        sut.resolveArcheageServer(requestMock) shouldBe archeageServer

        verifyAll {
            repositoryMock.findById(archeageServer.id!!)
        }
    }

    @Test
    fun `should store the archeage server as cookie header`() {
        val archeageServer = ArcheageServer("ANY",1)
        val capturedValue = slot<String>()
        every { responseMock.addHeader(HttpHeaders.SET_COOKIE, capture(capturedValue)) } just runs

        sut.setArcheageServer(responseMock,archeageServer)

        verifyAll { responseMock.addHeader(HttpHeaders.SET_COOKIE,any()) }
        capturedValue.captured shouldBe SERVER_COOKIE.mutate().value("${archeageServer.id}").build().toString()
    }
}