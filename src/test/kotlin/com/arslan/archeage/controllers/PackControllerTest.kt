package com.arslan.archeage.controllers

import com.arslan.archeage.*
import com.arslan.archeage.entity.Price
import com.arslan.archeage.service.ArcheageServerContextHolder
import io.mockk.*
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request

class PackControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest(){

    private val archeageServer = availableServers[0]
    private val pageable = PageRequest.of(2,3)
    private val packRequest = PackRequest(Continent.WEST,null,null,null)
    private val packPercentageUpdate = PackPercentageUpdate(1,120)
    private lateinit var packsPage: Page<PackDTO>

    @BeforeEach
    fun setUpTestContext(){
        ArcheageServerContextHolder.setServerContext(archeageServer)
        packsPage = PageImpl(
            listOf(
                PackDTO("PACK_1","ANY_LOC","ANY_DEST", Price(3,24,25),6, emptyList(),1,Price(52,23,11),Price(10,1,5)),
                PackDTO("PACK_2","ANY_LOC","ANY_DEST", Price(200,1,4),9, emptyList(),2,Price(44,55,0),Price(12,13,20)),
                PackDTO("PACK_3","ANY_LOC","ANY_DEST", Price(0,20,30),10, emptyList(),3,Price(1,12,15),Price(11,2,4))
            ),
            pageable,100
        )
    }

    @Test
    fun `should return 400 bad request if archeage server is not set`() {
        ArcheageServerContextHolder.clear()

        mockMvc
            .get("/pack?continent=${Continent.WEST}")
            .andExpect {
                status { isBadRequest() }
                content { string(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),LocaleContextHolder.getLocale())) }
            }

        verifyAll { packServiceMock wasNot called }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested continent`(continent: Continent) {
        val expectedRequest = packRequest.copy(continent = continent)
        every { packServiceMock.packs(expectedRequest,pageable,archeageServer) } returns packsPage

        mockMvc
            .get("/pack?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(expectedRequest,pageable,archeageServer) }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested destination and continent`(continent: Continent) {
        val expectedPackRequest = packRequest.copy(continent = continent, destinationLocation = 1)
        every { packServiceMock.packs(expectedPackRequest,pageable,archeageServer) } returns packsPage

        mockMvc
            .get("/pack?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}&destinationLocation=1")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(expectedPackRequest,pageable,archeageServer) }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested departure and continent`(continent: Continent) {
        val expectedPackRequest = packRequest.copy(continent = continent, departureLocation = 5)
        every { packServiceMock.packs(expectedPackRequest,pageable,archeageServer) } returns packsPage

        mockMvc
            .get("/pack?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}&departureLocation=5")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(expectedPackRequest,pageable,archeageServer)  }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested departure and destination location and continent`(continent: Continent) {
        val expectedPackRequest = packRequest.copy(continent = continent, destinationLocation = 1, departureLocation = 3)
        every { packServiceMock.packs(expectedPackRequest,pageable,archeageServer) } returns packsPage

        mockMvc
            .get("/pack?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}&departureLocation=3&destinationLocation=1")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(expectedPackRequest,pageable,archeageServer)  }
    }

    @MethodSource("contentTypesOtherThanJson")
    @ParameterizedTest
    fun `should return 406(Not Acceptable) when request accept content type is invalid`(invalidContentType: MediaType) {
        mockMvc
            .get("/pack"){
                accept = invalidContentType
            }.andExpect {
                status { isNotAcceptable() }
            }

        verifyAll { packServiceMock wasNot called }
    }

    @MethodSource("httpMethodsOtherThanGet")
    @ParameterizedTest
    fun `should return 403(Forbidden) when requesting packs with http method other than GET`(invalidHttpMethod: HttpMethod) {
        mockMvc
            .request(invalidHttpMethod,"/pack")
            .andExpect { status { isForbidden() } }

        verifyAll { packServiceMock wasNot called }
    }

    @Test
    fun `should return 302 redirect response to login when trying to update pack percentage without being authenticated`() {
        mockMvc
            .put("/pack/profit"){
                with(csrf())
                content = json.encodeToString(packPercentageUpdate)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { is3xxRedirection() }
            }

        verifyAll {
            packProfitServiceMock wasNot called
        }
    }

    @WithMockUser("1")
    @MethodSource("contentTypesOtherThanJson")
    @ParameterizedTest
    fun `should return 415(Unsupported Media Type) when trying to update pack percentage with invalid content type`(invalidContentType: MediaType) {
        mockMvc
            .put("/pack/profit"){
                with(csrf())
                content = json.encodeToString(packPercentageUpdate)
                contentType = invalidContentType
            }.andExpect {
                status { isUnsupportedMediaType() }
            }

        verifyAll {
            packProfitServiceMock wasNot called
        }
    }

    @WithMockUser("1")
    @Test
    fun `should return 403(Forbidden) when trying to update pack percentage without csrf`() {
        mockMvc
            .put("/pack/profit"){
                content = json.encodeToString(packPercentageUpdate)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isForbidden() }
            }

        verifyAll {
            packProfitServiceMock wasNot called
        }
    }

    @WithMockUser("1")
    @Test
    fun `should return 200(OK) and update pack percentage`() {
        every { packProfitServiceMock.updatePercentage(packPercentageUpdate.copy(userID = 1)) } just runs

        mockMvc
            .put("/pack/profit"){
                with(csrf())
                content = json.encodeToString(packPercentageUpdate)
                contentType = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isOk() }
            }

        verifyAll {
            packProfitServiceMock.updatePercentage(packPercentageUpdate.copy(userID = 1))
        }
    }


}