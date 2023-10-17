package com.arslan.web.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

class ResourceControllerTest @Autowired constructor(private val mockMvc: MockMvc) : AbstractControllerTest() {

    @Test
    fun `should return 404 not found when trying to get not existing resource`() {
        mockMvc
            .get("/resource/not_existing_resource")
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `should return 200(OK) with resource`() {
        mockMvc
            .get("/resource/test")
            .andExpect {
                status { isOk() }
                content { string("test") }
            }
    }
}