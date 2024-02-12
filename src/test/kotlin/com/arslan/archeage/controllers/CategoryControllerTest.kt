package com.arslan.archeage.controllers

import com.arslan.archeage.CategoryDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ArcheageServerContextHolder
import io.mockk.called
import io.mockk.every
import io.mockk.verifyAll
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CategoryControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest() {

    private lateinit var archeageServer: ArcheageServer

    @BeforeEach
    fun setUp2() {
        archeageServer = availableServers[0]
        ArcheageServerContextHolder.setServerContext(archeageServer)
    }

    @Test
    fun `should return 400(Bad Request) if no archeage server is selected`() {
        ArcheageServerContextHolder.clear()

        mockMvc
            .get("/category")
            .andExpect { status { isBadRequest() } }

        verifyAll { categoryService wasNot called }
    }

    @MethodSource("contentTypesOtherThanJson")
    @ParameterizedTest
    fun `should return 406(not acceptable) when trying to get categories with invalid accept header`(invalidMediaType: MediaType) {
        mockMvc
            .get("/category"){
                accept = invalidMediaType
            }.andExpect {
                status { isNotAcceptable() }
            }

        verifyAll { categoryService wasNot called }
    }

    @MethodSource("httpMethodsOtherThanGet")
    @ParameterizedTest
    fun `should return 403(forbidden) when trying to use any method other than get to request categories`(invalidHttpMethod: HttpMethod) {
        mockMvc
            .perform { MockMvcRequestBuilders.request(invalidHttpMethod,"/category").buildRequest(it) }
            .andExpect(status().isForbidden)

        verifyAll {
            categoryService wasNot called
        }
    }

    @Test
    fun `should return 200(OK) with categories`() {
        val expectedCategories = listOf(
            CategoryDTO(1,"ANY_CATEGORY_1", mutableListOf(CategoryDTO(2,"ANY_SUBCATEGORY_1"))),
            CategoryDTO(3,"ANY_CATEGORY_2")
        )

        every { categoryService.categories(archeageServer) } returns expectedCategories

        mockMvc
            .get("/category")
            .andExpect {
                status { isOk() }
                content { json(json.encodeToString(expectedCategories)) }
            }

        verifyAll {
            categoryService.categories(archeageServer)
        }
    }

}