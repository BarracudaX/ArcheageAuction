package com.arslan.archeage.unit

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ArcheageServerContextHolder
import com.arslan.archeage.web.ArcheageServerHandlerMethodArgumentResolver
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest

class ArcheageServerHandlerMethodArgumentResolverTest {

    private val methodParameterMock = mockk<MethodParameter>()
    private val webRequestDummy = mockk<NativeWebRequest>()
    private val sut = ArcheageServerHandlerMethodArgumentResolver()

    @Test
    fun `should return true when requested for support and method parameter has type assignable from ArcheageServer class`() {
        every { methodParameterMock.parameterType } returns String::class.java
        sut.supportsParameter(methodParameterMock) shouldBe false

        every { methodParameterMock.parameterType } returns ArcheageServer::class.java
        sut.supportsParameter(methodParameterMock) shouldBe true
    }

    @Test
    fun `should throw ArcheageContextHolderEmptyException when trying to resolve archeage server parameter and context is empty`() {
        ArcheageServerContextHolder.clear()

        shouldThrow<ArcheageContextHolderEmptyException> { sut.resolveArgument(methodParameterMock,null,webRequestDummy,null) }
    }

    @Test
    fun `should return archeage server when resolving parameter and context is not empty`() {
        val archeageServer = ArcheageServer("ANY_1",1)
        ArcheageServerContextHolder.setServerContext(archeageServer)

        sut.resolveArgument(methodParameterMock,null,webRequestDummy,null) shouldBe archeageServer
    }
}