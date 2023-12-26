package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.Packs
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Price
import com.arslan.archeage.service.ArcheageServerContextHolder
import io.mockk.called
import io.mockk.every
import io.mockk.verifyAll
import kotlinx.serialization.encodeToString
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request

class PackControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest(){

    private lateinit var archeageServer: ArcheageServer
    private val pageable = PageRequest.of(2,3)
    private lateinit var packsPage: Page<PackDTO>

    @BeforeEach
    fun setUpTestContext(){
        archeageServer = availableServers[0]
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
            .get("/packs")
            .andExpect {
                status { isBadRequest() }
                content { string(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),LocaleContextHolder.getLocale())) }
            }

        verifyAll { packServiceMock wasNot called }
    }


    @Test
    fun `should return packs of default continent`() {
        every { packServiceMock.packs(Continent.values()[0],archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(Continent.values()[0],archeageServer,pageable) }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested continent`(continent: Continent) {
        every { packServiceMock.packs(continent,archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(continent,archeageServer,pageable) }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested destination and continent`(continent: Continent) {
        every { packServiceMock.packsSoldAt(continent,1,archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}&destinationLocation=1")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packsSoldAt(continent,1,archeageServer,pageable) }
    }

    @Test
    fun `should return packs of requested destination of default continent`() {
        every { packServiceMock.packsSoldAt(Continent.values()[0],1,archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}&destinationLocation=1")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packsSoldAt(Continent.values()[0],1,archeageServer,pageable) }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested departure and continent`(continent: Continent) {
        every { packServiceMock.packsCreatedAt(continent,5,archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}&departureLocation=5")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packsCreatedAt(continent,5,archeageServer,pageable)  }
    }

    @Test
    fun `should return packs of requested departure of default continent`() {
        every { packServiceMock.packsCreatedAt(Continent.values()[0],5,archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}&departureLocation=5")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packsCreatedAt(Continent.values()[0],5,archeageServer,pageable)  }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs of requested departure and destination location and continent`(continent: Continent) {
        every { packServiceMock.packs(1,continent,3,archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}&continent=${continent.name}&departureLocation=3&destinationLocation=1")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(1,continent,3,archeageServer,pageable)  }
    }

    @Test
    fun `should return packs of requested departure and destination location of default continent`() {
        every { packServiceMock.packs(1,Continent.values()[0],3,archeageServer,pageable) } returns packsPage

        mockMvc
            .get("/packs?size=${pageable.pageSize}&page=${pageable.pageNumber}&departureLocation=3&destinationLocation=1")
            .andExpect {
                status { isOk() }
                content {
                    json(json.encodeToString(Packs(packsPage.content,packsPage.hasNext(),packsPage.hasPrevious())))
                }
            }

        verifyAll { packServiceMock.packs(1,Continent.values()[0],3,archeageServer,pageable)  }
    }

    @MethodSource("contentTypesOtherThanJson")
    @ParameterizedTest
    fun `should return 406(Not Acceptable) when request accept content type is invalid`(invalidContentType: MediaType) {
        mockMvc
            .get("/packs"){
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
            .request(invalidHttpMethod,"/packs")
            .andExpect { status { isForbidden() } }

        verifyAll { packServiceMock wasNot called }
    }
}