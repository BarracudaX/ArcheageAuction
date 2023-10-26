package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Region
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ArcheageServerContextHolderTest {

    private val expected = ArcheageServer("TEST", Region.CIS,1)

    companion object{

        @AfterAll
        fun clear(){
            ArcheageServerContextHolder.clear()
        }

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