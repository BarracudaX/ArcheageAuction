package com.arslan.archeage.controllers

import com.arslan.archeage.Continent
import com.arslan.archeage.LocationDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.service.ArcheageServerContextHolder
import io.mockk.called
import io.mockk.every
import io.mockk.verifyAll
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request

class LocationControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest() {


    private lateinit var usersArcheageServer: ArcheageServer
    private val locations = mutableListOf<Location>()
    private val factories = mutableListOf<Location>()

    @BeforeEach
    fun setUpTestContext(){
        usersArcheageServer = availableServers[0]
        locations.add(Location("LOCATION_1",Continent.WEST,usersArcheageServer,false,1))
        locations.add(Location("LOCATION_2",Continent.WEST,usersArcheageServer,true,2))
        locations.add(Location("LOCATION_3",Continent.WEST,usersArcheageServer,true,3))

        factories.add(Location("FACTORY_1",Continent.WEST,usersArcheageServer,true,4))
        factories.add(locations[1])
        factories.add(locations[2])

        ArcheageServerContextHolder.setServerContext(usersArcheageServer)

        every { locationServiceMock.continentLocations(Continent.WEST,usersArcheageServer) } returns locations
        every { locationServiceMock.continentFactories(Continent.WEST,usersArcheageServer) } returns factories
    }

    @Test
    fun `should return 400 bad request if archeage server is not set`() {
        ArcheageServerContextHolder.clear()

        mockMvc
            .get("/locations?continent=${Continent.WEST}")
            .andExpect {
                status { isBadRequest() }
                content { string(messageSource.getMessage("archeage.server.not.chosen.error.message", emptyArray(),LocaleContextHolder.getLocale())) }
            }

        verifyAll { locationServiceMock wasNot called }
    }

    @MethodSource("invalidContinentValues")
    @ParameterizedTest
    fun `should return 400 bad request if continent value is invalid`(invalidContinentValue: String) {
        mockMvc
            .get("/locations?continent=$invalidContinentValue")
            .andExpect { status { isBadRequest() } }

        verifyAll { locationServiceMock wasNot called }
    }

    @Test
    fun `should return 400 bad request if continent query parameter is not present`() {
        mockMvc
            .get("/locations")
            .andExpect { status { isBadRequest() } }

        verifyAll { locationServiceMock wasNot called }
    }

    @Test
    fun `should not return continent locations`() {
        mockMvc
            .get("/locations?continent=${Continent.WEST}")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.continentLocations",Matchers.hasSize<LocationDTO>(locations.size))
                    jsonPath("$.continentFactories",Matchers.hasSize<LocationDTO>(factories.size))
                    locations.map(Location::name).forEach { location -> jsonPath("$.continentLocations[*].name",Matchers.hasItem(location)) }
                    locations.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentLocations[*].id",Matchers.hasItem(location)) }
                    factories.map(Location::name).forEach { location -> jsonPath("$.continentFactories[*].name",Matchers.hasItem(location)) }
                    factories.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentFactories[*].id",Matchers.hasItem(location)) }
                }
            }

        verifyAll {
            locationServiceMock.continentLocations(Continent.WEST,usersArcheageServer)
            locationServiceMock.continentFactories(Continent.WEST,usersArcheageServer)
        }
    }

    /**
     * Should exclude a departure location that has the same id as the id of the query parameter with name destinationLocation.
     * This is done because locations are used for pack selection, and pack cannot be created(departure location) in the same location where it is sold(destination location).
     */
    @Test
    fun `should filter out departure location if destination location is provided`() {
        val expectedLocations = locations.filter { it.id != factories[2].id }
        mockMvc
            .get("/locations?continent=${Continent.WEST}&destinationLocation=${factories[2].id}")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.continentLocations",Matchers.hasSize<LocationDTO>(expectedLocations.size))
                    jsonPath("$.continentFactories",Matchers.hasSize<LocationDTO>(factories.size))
                    expectedLocations.map(Location::name).forEach { location -> jsonPath("$.continentLocations[*].name",Matchers.hasItem(location)) }
                    expectedLocations.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentLocations[*].id",Matchers.hasItem(location)) }
                    factories.map(Location::name).forEach { location -> jsonPath("$.continentFactories[*].name",Matchers.hasItem(location)) }
                    factories.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentFactories[*].id",Matchers.hasItem(location)) }
                }
            }

        verifyAll {
            locationServiceMock.continentLocations(Continent.WEST, usersArcheageServer)
            locationServiceMock.continentFactories(Continent.WEST, usersArcheageServer)
        }
    }

    /**
     * Should exclude destination location that has the same id as the id of the query parameter with name departureLocation.
     * This is done because locations are used for pack selection, and pack cannot be sold(destination location) in the same location where it is created(departure location).
     */
    @Test
    fun `should filter out destination location if departure location is provided`() {
        val expectedFactories = factories.filter { it.id !=  locations[2].id }
        mockMvc
            .get("/locations?continent=${Continent.WEST}&departureLocation=${locations[2].id}")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.continentLocations",Matchers.hasSize<LocationDTO>(locations.size))
                    jsonPath("$.continentFactories",Matchers.hasSize<LocationDTO>(expectedFactories.size))
                    locations.map(Location::name).forEach { location -> jsonPath("$.continentLocations[*].name",Matchers.hasItem(location)) }
                    locations.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentLocations[*].id",Matchers.hasItem(location)) }
                    expectedFactories.map(Location::name).forEach { location -> jsonPath("$.continentFactories[*].name",Matchers.hasItem(location)) }
                    expectedFactories.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentFactories[*].id",Matchers.hasItem(location)) }
                }
            }

        verifyAll {
            locationServiceMock.continentLocations(Continent.WEST, usersArcheageServer)
            locationServiceMock.continentFactories(Continent.WEST, usersArcheageServer)
        }
    }

    /**
     * This test is combination of the tests (1)should filter out destination location if departure location is provided and (2)should filter out departure location if destination location is provided
     */
    @Test
    fun `should filter out destination location and departure location`() {
        val expectedFactories = factories.filter { it.id !=  locations[1].id }
        val expectedLocations = locations.filter { it.id != factories[2].id }
        mockMvc
            .get("/locations?continent=${Continent.WEST}&destinationLocation=${factories[2].id}&departureLocation=${locations[1].id}")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.continentLocations",Matchers.hasSize<LocationDTO>(expectedLocations.size))
                    jsonPath("$.continentFactories",Matchers.hasSize<LocationDTO>(expectedFactories.size))
                    expectedLocations.map(Location::name).forEach { location -> jsonPath("$.continentLocations[*].name",Matchers.hasItem(location)) }
                    expectedLocations.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentLocations[*].id",Matchers.hasItem(location)) }
                    expectedFactories.map(Location::name).forEach { location -> jsonPath("$.continentFactories[*].name",Matchers.hasItem(location)) }
                    expectedFactories.map(Location::id).map { it!!.toInt() }.forEach { location -> jsonPath("$.continentFactories[*].id",Matchers.hasItem(location)) }
                }
            }

        verifyAll {
            locationServiceMock.continentLocations(Continent.WEST, usersArcheageServer)
            locationServiceMock.continentFactories(Continent.WEST, usersArcheageServer)
        }
    }

    @MethodSource("contentTypesOtherThanJson")
    @ParameterizedTest
    fun `should return 406 response status when request has non-acceptable accept header`(nonAcceptableContentType: MediaType) {
        mockMvc
            .get("/locations?continent=${Continent.WEST}"){
                accept = nonAcceptableContentType
            }.andExpect {
                status { isNotAcceptable() }
            }

        verifyAll { locationServiceMock wasNot called }
    }

    @MethodSource("httpMethodsOtherThanGet")
    @ParameterizedTest
    fun `should return 403(forbidden) when trying any method other than GET`(invalidHttpMethod: HttpMethod) {
        mockMvc
            .request(invalidHttpMethod,"/locations?continent=${Continent.WEST}")
            .andExpect { status { isForbidden() } }

        verifyAll { locationServiceMock wasNot called }
    }
}