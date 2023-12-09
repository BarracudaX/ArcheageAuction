package com.arslan.archeage.controllers

import com.arslan.archeage.*
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.Region
import com.arslan.archeage.service.ArcheageServerContextHolder
import io.mockk.every
import io.mockk.mockk
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

    private val viewEndpoints = arrayOf("/login","/register","/packs")
    private val packs = listOf(PackDTO("ANY_PACK_NAME","ANY_CREATION_LOCATION","ANY_DESTINATION_LOCATION", Price(1,1,1),
        RecipeDTO(1,listOf(CraftingMaterialDTO(1, ItemDTO("ANY_ITEM_NAME",1),Price(2,2,2)),CraftingMaterialDTO(1, ItemDTO("ANY_ITEM_NAME_2",2))),1)
    ))
    private val defaultContinent = Continent.values()[0]

    private lateinit var usersArcheageServer: ArcheageServer

    @BeforeEach
    fun setUpTestContext(){
        usersArcheageServer = availableServers[Region.CIS]!![0]
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

    @Test
    fun `should return packs view with all required model attributes containing all packs using default continent`() {
        val continentLocations = createContinentLocations(defaultContinent)
        val continentFactories = createContinentFactories(defaultContinent)
        every { packServiceMock.packs(defaultContinent,usersArcheageServer) } returns packs
        every { locationServiceMock.continentLocations(defaultContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(defaultContinent) } returns continentFactories

        mockMvc
            .get("/packs")
            .andExpect {
                commonPacksAssertions(defaultContinent,continentLocations,continentFactories)
            }
        verifyAll {
            packServiceMock.packs(defaultContinent,usersArcheageServer)
            locationServiceMock.continentLocations(defaultContinent)
            locationServiceMock.continentFactories(defaultContinent)
        }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs view with all required model attributes containing all packs of the requested continent`(requestedContinent: Continent) {
        val continentLocations = createContinentLocations(requestedContinent)
        val continentFactories = createContinentFactories(requestedContinent)
        every { packServiceMock.packs(requestedContinent,usersArcheageServer) } returns packs
        every { locationServiceMock.continentLocations(requestedContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(requestedContinent) } returns continentFactories

        mockMvc
            .get("/packs?continent=${requestedContinent.name}")
            .andExpect {
                commonPacksAssertions(requestedContinent,continentLocations,continentFactories)
            }
        verifyAll {
            packServiceMock.packs(requestedContinent,usersArcheageServer)
            locationServiceMock.continentLocations(requestedContinent)
            locationServiceMock.continentFactories(requestedContinent)
        }
    }

    /**
     * Model attribute that indicates the available factories has a factory removed from the list if a factory is the chosen departure location,
     * that's the reason I am using one of continent factories as departure locations to test that it is removed.
     */
    @Test
    fun `should return packs view with all required model attributes containing packs with the requested departure location and default continent`() {
        val continentLocations = createContinentLocations(defaultContinent)
        val continentFactories = createContinentFactories(defaultContinent)
        val departureLocation = continentFactories[2].name
        every { packServiceMock.packsCreatedAt(defaultContinent,departureLocation,usersArcheageServer) } returns packs
        every { locationServiceMock.continentLocations(defaultContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(defaultContinent) } returns continentFactories

        mockMvc
            .get("/packs?departureLocation=${departureLocation}")
            .andExpect {
                commonPacksAssertions(defaultContinent,continentLocations,continentFactories, Optional.of(departureLocation))
            }

        verifyAll {
            packServiceMock.packsCreatedAt(defaultContinent,departureLocation,usersArcheageServer)
            locationServiceMock.continentLocations(defaultContinent)
            locationServiceMock.continentFactories(defaultContinent)
        }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs view with all required model attributes containing packs with the requested departure location and requested continent`(requestedContinent: Continent) {
        val continentLocations = createContinentLocations(requestedContinent)
        val continentFactories = createContinentFactories(requestedContinent)
        val departureLocation = continentFactories[2].name
        every { packServiceMock.packsCreatedAt(requestedContinent,departureLocation,usersArcheageServer) } returns packs
        every { locationServiceMock.continentLocations(requestedContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(requestedContinent) } returns continentFactories

        mockMvc
            .get("/packs?departureLocation=${departureLocation}&continent=${requestedContinent}")
            .andExpect {
                commonPacksAssertions(requestedContinent,continentLocations,continentFactories, Optional.of(departureLocation))
            }

        verifyAll {
            packServiceMock.packsCreatedAt(requestedContinent,departureLocation,usersArcheageServer)
            locationServiceMock.continentLocations(requestedContinent)
            locationServiceMock.continentFactories(requestedContinent)
        }
    }

    /**
     * Model attribute that indicates the locations has a location removed if it's chosen as a destination location,
     * that's the reason why I am using one of the continent locations as destination location to test for removal as well.
     */
    @Test
    fun `should return packs view with all required model attributes containing packs with the requested destination location and default continent`() {
        val continentLocations = createContinentLocations(defaultContinent)
        val continentFactories = createContinentFactories(defaultContinent)
        val destinationLocation = continentLocations[2].name
        every { packServiceMock.packsSoldAt(defaultContinent,destinationLocation,usersArcheageServer) } returns packs
        every { locationServiceMock.continentLocations(defaultContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(defaultContinent) } returns continentFactories

        mockMvc
            .get("/packs?destinationLocation=${destinationLocation}")
            .andExpect {
                commonPacksAssertions(defaultContinent,continentLocations,continentFactories, Optional.empty(),Optional.of(destinationLocation))
            }

        verifyAll {
            packServiceMock.packsSoldAt(defaultContinent,destinationLocation,usersArcheageServer)
            locationServiceMock.continentLocations(defaultContinent)
            locationServiceMock.continentFactories(defaultContinent)
        }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs view with all required model attributes containing packs with the requested destination location and requested continent`(requestedContinent: Continent) {
        val continentLocations = createContinentLocations(requestedContinent)
        val continentFactories = createContinentFactories(requestedContinent)
        val destinationLocation = continentLocations[2].name
        every { packServiceMock.packsSoldAt(requestedContinent,destinationLocation,usersArcheageServer) } returns packs
        every { locationServiceMock.continentLocations(requestedContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(requestedContinent) } returns continentFactories

        mockMvc
            .get("/packs?destinationLocation=${destinationLocation}&continent=${requestedContinent.name}")
            .andExpect {
                commonPacksAssertions(requestedContinent,continentLocations,continentFactories, Optional.empty(),Optional.of(destinationLocation))
            }

        verifyAll {
            packServiceMock.packsSoldAt(requestedContinent,destinationLocation,usersArcheageServer)
            locationServiceMock.continentLocations(requestedContinent)
            locationServiceMock.continentFactories(requestedContinent)
        }
    }

    @Test
    fun `should return packs view with all required model attributes containing packs with for the requested destination and departure locations and default continent`() {
        val continentLocations = createContinentLocations(defaultContinent)
        val continentFactories = createContinentFactories(defaultContinent)
        val destinationLocation = continentLocations[2].name
        val departureLocation = continentFactories[2].name
        every { locationServiceMock.continentLocations(defaultContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(defaultContinent) } returns continentFactories
        every { packServiceMock.packs(defaultContinent,departureLocation,destinationLocation,usersArcheageServer) } returns packs

        mockMvc
            .get("/packs?destinationLocation=${destinationLocation}&departureLocation=${departureLocation}")
            .andExpect {
                commonPacksAssertions(defaultContinent,continentLocations,continentFactories,Optional.of(departureLocation), Optional.of(destinationLocation))
            }

        verifyAll {
            packServiceMock.packs(defaultContinent,departureLocation,destinationLocation,usersArcheageServer)
            locationServiceMock.continentLocations(defaultContinent)
            locationServiceMock.continentFactories(defaultContinent)
        }
    }

    @MethodSource("continents")
    @ParameterizedTest
    fun `should return packs view with all required model attributes containing packs with for the requested destination and departure locations and requested continent`(requestedContinent: Continent) {
        val continentLocations = createContinentLocations(requestedContinent)
        val continentFactories = createContinentFactories(requestedContinent)
        val destinationLocation = continentLocations[2].name
        val departureLocation = continentFactories[2].name
        every { locationServiceMock.continentLocations(requestedContinent) } returns continentLocations
        every { locationServiceMock.continentFactories(requestedContinent) } returns continentFactories
        every { packServiceMock.packs(requestedContinent,departureLocation,destinationLocation,usersArcheageServer) } returns packs

        mockMvc
            .get("/packs?destinationLocation=${destinationLocation}&departureLocation=${departureLocation}&continent=${requestedContinent.name}")
            .andExpect {
                commonPacksAssertions(requestedContinent,continentLocations,continentFactories,Optional.of(departureLocation), Optional.of(destinationLocation))
            }

        verifyAll {
            packServiceMock.packs(requestedContinent,departureLocation,destinationLocation,usersArcheageServer)
            locationServiceMock.continentLocations(requestedContinent)
            locationServiceMock.continentFactories(requestedContinent)
        }
    }

    private fun createContinentLocations(continent: Continent) : List<Location> = listOf(
        Location("ANY_CONTINENT_LOCATION_1",continent, Region.EUROPE,false,1),
        Location("ANY_CONTINENT_LOCATION_2",continent, Region.EUROPE,false,2),
        Location("ANY_CONTINENT_LOCATION_3",continent,Region.EUROPE,false,6)
    )

    private fun createContinentFactories(continent: Continent) : List<Location> = listOf(
        Location("ANY_CONTINENT_FACTORY_LOCATION_1",continent, Region.EUROPE,true,3),
        Location("ANY_CONTINENT_FACTORY_LOCATION_2",continent, Region.EUROPE,true,4),
        Location("ANY_CONTINENT_FACTOR_LOCATION_3",continent,Region.EUROPE,true,5)
    )

    private fun MockMvcResultMatchersDsl.commonPacksAssertions(continent: Continent, continentLocations: List<Location>, continentFactories: List<Location>, departureLocation: Optional<String> = Optional.empty(), destinationLocation: Optional<String> = Optional.empty()){
        status { isOk() }
        view { name("packs") }
        model {
            attribute("packs",packs)
            attribute("locations",continentLocations.map(Location::name).minus(destinationLocation.getOrElse { "" }))
            attribute("factories",continentFactories.map(Location::name).minus(departureLocation.getOrElse { "" }))
            attribute("selectedContinent",continent)
            attribute("departureLocation",departureLocation)
            attribute("destinationLocation",destinationLocation)
            attribute("materials",packs.materialsWithPrice())
            assertCommonModelAttributesForAllControllers(usersArcheageServer)
            size(7+numberOfCommonModelAttributes)
        }
    }
}