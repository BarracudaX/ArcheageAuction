package com.arslan.archeage.controllers

import com.arslan.archeage.*
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.ArcheageServerContextHolder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

class ViewControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest() {

    private val publicViewPoints = arrayOf("/login","/register","/packs_view","/")

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
        for(viewEndpoint in publicViewPoints){
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
            .get("/user")
            .andExpect { status { isForbidden() } }
    }

    @Test
    fun `should return home page`() {
        mockMvc
            .get("/")
            .andExpect {
                status { isOk() }
                content { view { name("index") } }
            }
    }

    @Test
    fun `should return packs view`() {
        mockMvc
            .get("/packs_view")
            .andExpect {
                status { isOk() }
                content { view { name("packs") } }
            }
    }

    @Test
    fun `should redirect to login page when requesting profile page without being authenticated`() {
        mockMvc
            .get("/profile")
            .andExpect {
                status { is3xxRedirection() }
            }
    }

    @WithMockUser
    @Test
    fun `should return profile page if user is authenticated`() {
        mockMvc
            .get("/profile")
            .andExpect {
                status { isOk() }
                view { name("profile") }
            }
    }

    @WithMockUser
    @MethodSource("contentTypesOtherThanHTML")
    @ParameterizedTest
    fun `should return 406 not acceptable when trying to get profile view with accept type other than html`(invalidMediaType: MediaType) {
        mockMvc
            .get("/profile"){
                accept = invalidMediaType
            }.andExpect {
                status { isNotAcceptable() }
            }
    }
}