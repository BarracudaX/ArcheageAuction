package com.arslan.archeage.controllers

import com.arslan.archeage.ItemDTO
import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.UserPrices
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.User
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.service.ArcheageServerContextHolder
import io.mockk.*
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class UserPriceControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest() {

    private val archeageServer: ArcheageServer = availableServers[0]
    private val pageable = PageRequest.of(2,10)
    private val user = User("ANY","ANY")
    private val userPrices = PageImpl(
        listOf(
            UserPrice(UserPriceKey(user,PurchasableItem("ANY","ANY",archeageServer).apply { id = 1 }), Price(1,2,3)),
            UserPrice(UserPriceKey(user,PurchasableItem("ANY_2","ANY_2",archeageServer).apply { id = 2 }), Price(4,5,6)),
        ),
        pageable, 100
    )
    private val itemDTOs = userPrices.content.map { ItemDTO(it.id.purchasableItem.name,it.id.purchasableItem.id!!,it.price) }
    private val expectedJson = json.encodeToString(UserPrices(itemDTOs,userPrices.hasNext(),userPrices.hasPrevious()))

    @BeforeEach
    fun setUpTestContext(){
        ArcheageServerContextHolder.setServerContext(archeageServer)
        every { itemPriceServiceMock.userPrices(1,pageable) } returns userPrices
    }

    @WithAnonymousUser
    @Test
    fun `should redirect when trying to request user prices without being authenticated`() {
        mockMvc
            .get("/user/price")
            .andExpect { status { is3xxRedirection() } }

        verifyAll {
            itemPriceServiceMock wasNot called
        }
    }

    @WithMockUser("1")
    @Test
    fun `should return 400(Bad Request) if archeage server is not set`() {
        ArcheageServerContextHolder.clear()

        mockMvc
            .get("/user/price")
            .andExpect {
                status { isBadRequest() }
                content { string(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),LocaleContextHolder.getLocale())) }
            }

        verifyAll {
            itemPriceServiceMock wasNot called
            itemService wasNot called
        }
    }

    @Test
    fun `should return user prices`() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext().apply { authentication = UsernamePasswordAuthenticationToken(1L,"") })
        mockMvc
            .get("/user/price?page=${pageable.pageNumber}&size=${pageable.pageSize}")
            .andExpect {
                status { isOk() }
                content { json(expectedJson) }
            }

        verifyAll { itemPriceServiceMock.userPrices(1,pageable) }
    }

    @WithAnonymousUser
    @Test
    fun `should return 403(Forbidden) when trying to post user price without being authenticated`() {
        mockMvc
            .post("/user/price")
            .andExpect { status { isForbidden() } }

        verifyAll { itemPriceServiceMock wasNot called }
    }

    @Test
    fun `should save user price with user id replaced with id of authenticated user and return 200(OK) response with success message`() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext().apply { authentication = UsernamePasswordAuthenticationToken(1L,"") })
        val price = UserPriceDTO(10,1,Price(10,5,5))
        every { itemPriceServiceMock.saveUserPrice(price.copy(userID = 1)) } just runs

        mockMvc
            .post("/user/price"){
                contentType = MediaType.APPLICATION_JSON
                content = json.encodeToString(price)
                with(SecurityMockMvcRequestPostProcessors.csrf())
            }.andExpect {
                status { isOk() }
                content { string(messageSource.getMessage("price.save.success", emptyArray(),LocaleContextHolder.getLocale())) }
            }

        verifyAll { itemPriceServiceMock.saveUserPrice(price.copy(userID = 1)) }
    }

    @WithMockUser("1")
    @Test
    fun `should return 403(Forbidden) when trying to save user price without csrf token`() {
        val price = UserPriceDTO(10,1,Price(10,5,5))

        mockMvc
            .post("/user/price"){
                contentType = MediaType.APPLICATION_JSON
                content = json.encodeToString(price)
            }.andExpect {
                status { isForbidden() }
            }

        verifyAll { itemPriceServiceMock wasNot called }
    }


    @WithMockUser("1")
    @MethodSource("contentTypesOtherThanJson")
    @ParameterizedTest
    fun `should return 415(Unsupported Media Type) when trying to save user price with invalid request content type`(invalidContentType: MediaType) {
        val price = UserPriceDTO(10,1,Price(10,5,5))

        mockMvc
            .post("/user/price"){
                contentType = invalidContentType
                content = json.encodeToString(price)
                with(SecurityMockMvcRequestPostProcessors.csrf())
            }.andExpect {
                status { isUnsupportedMediaType() }
            }

        verifyAll { itemPriceServiceMock wasNot called }
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }
}