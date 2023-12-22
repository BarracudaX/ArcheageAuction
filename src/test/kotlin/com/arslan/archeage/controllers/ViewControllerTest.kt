package com.arslan.archeage.controllers

import com.arslan.archeage.*
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.service.ArcheageServerContextHolder
import io.mockk.every
import io.mockk.verifyAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl
import org.springframework.test.web.servlet.get
import java.util.Optional
import kotlin.jvm.optionals.getOrElse

class ViewControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest() {

    private val viewEndpoints = arrayOf("/login","/register","/packs_view")

    private lateinit var usersArcheageServer: ArcheageServer

    @BeforeEach
    fun setUpTestContext(){
        usersArcheageServer = availableServers[0]
        ArcheageServerContextHolder.setServerContext(usersArcheageServer)
    }

    @WithAnonymousUser
    @Test
    fun `should return login page when requesting login page and user not authenticated`() {
        mockMvc
            .get("/login")
            .andExpect {
                status { isOk() }
                view { name("login") }
            }
    }

    @WithMockUser
    @Test
    fun `should return 403(Forbidden) when trying to access login page while being authenticated`() {
        mockMvc
            .get("/login")
            .andExpect { status { isForbidden() } }
    }

    @MethodSource("contentTypesOtherThanHTML")
    @ParameterizedTest
    fun `should return 406 not acceptable when trying to get any view with accept type other than html`(invalidMediaType: MediaType) {
        for(viewEndpoint in viewEndpoints){
            mockMvc
                .get(viewEndpoint){ accept = invalidMediaType }
                .andExpect { status { isNotAcceptable() } }
        }
    }

    @WithAnonymousUser
    @Test
    fun `should return register page when requesting registration page and user not authenticated`() {
        mockMvc
            .get("/register")
            .andExpect {
                status { isOk() }
                view { name("register") }
                model { attributeExists("registrationForm") }
            }
    }

    @WithMockUser
    @Test
    fun `should return 403(Forbidden) when user requests registration page while being authenticated`() {
        mockMvc
            .get("/register")
            .andExpect { status { isForbidden() } }
    }

}