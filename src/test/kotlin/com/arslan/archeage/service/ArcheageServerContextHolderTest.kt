package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ArcheageServerContextHolderTest {

    private val expected = ArcheageServer("TEST", 1)

    @BeforeEach
    fun setUp() {
        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should return null if no archeage server context was set`() {
        ArcheageServerContextHolder.getServerContext().shouldBeNull()
    }

    @Test
    fun `should return the set archeage server context`() {
        ArcheageServerContextHolder.setServerContext(expected)

        ArcheageServerContextHolder.getServerContext() shouldBe expected

        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should clear archeage server context on demand`() {
        ArcheageServerContextHolder.setServerContext(expected)

        ArcheageServerContextHolder.clear()

        ArcheageServerContextHolder.getServerContext().shouldBeNull()
    }
}