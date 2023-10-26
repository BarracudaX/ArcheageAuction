package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Region
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ArcheageServerContextHolderTest {

    private val expected = ArcheageServer("TEST", Region.CIS,1)

    @Test
    fun `should return null if no archeage server context was set`() {
        assertNull(ArcheageServerContextHolder.getServerContext())
    }

    @Test
    fun `should return the set archeage server context`() {
        ArcheageServerContextHolder.setServerContext(expected)

        assertEquals(expected,ArcheageServerContextHolder.getServerContext())

        ArcheageServerContextHolder.clear()
    }

    @Test
    fun `should clear archeage server context on demand`() {
        ArcheageServerContextHolder.setServerContext(expected)

        ArcheageServerContextHolder.clear()

        assertNull(ArcheageServerContextHolder.getServerContext())
    }
}