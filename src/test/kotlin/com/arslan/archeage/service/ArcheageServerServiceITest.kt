package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ArcheageServerServiceITest(
    private val archeageServerService: ArcheageServerService
) : AbstractITest() {

    @Test
    fun `should return empty map if no archeage server exists`() {
        assertTrue(archeageServerService.servers().isEmpty())
    }

    @Test
    fun `should return archeage servers`() {
        val expected = archeageServerRepository.saveAll(listOf(ArcheageServer("ANY_NAME_1"),ArcheageServer("ANY_NAME_2"),ArcheageServer("ANY_NAME_3")))

        archeageServerService.servers().shouldContainExactly(expected)
    }

}