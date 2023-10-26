package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Region
import com.arslan.archeage.repository.ArcheageServerRepository
import io.kotest.matchers.maps.shouldContainExactly
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
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
    fun `should return archeage servers grouped by region`() {
        val expected = mapOf(
            Region.CIS to listOf(archeageServerRepository.save(ArcheageServer("ANY_NAME_1",Region.CIS))),
            Region.EUROPE to archeageServerRepository.saveAll(listOf(ArcheageServer("ANY_NAME_2",Region.EUROPE),ArcheageServer("ANY_NAME_3",Region.EUROPE))),
        )

        archeageServerService.servers().shouldContainExactly(expected)
    }

}