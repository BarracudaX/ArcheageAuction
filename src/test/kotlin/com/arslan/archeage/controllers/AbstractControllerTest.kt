package com.arslan.archeage.controllers

import com.arslan.archeage.AbstractTest
import com.arslan.archeage.config.InfrastructureConfiguration
import com.arslan.archeage.config.SecurityConfiguration
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.service.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.result.ModelResultMatchersDsl


@Import(InfrastructureConfiguration::class,SecurityConfiguration::class)
@WebMvcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@ComponentScan(basePackages = ["com.arslan.archeage.web"])
abstract class AbstractControllerTest : AbstractTest() {

    @MockkBean
    protected lateinit var authenticationProviderMock: AuthenticationProvider

    @MockkBean
    protected lateinit var archeageServerServiceMock: ArcheageServerService

    @MockkBean
    protected lateinit var locationServiceMock: LocationService

    @MockkBean
    protected lateinit var packServiceMock: PackService

    @MockkBean
    protected lateinit var archeageServerRepositoryMock: ArcheageServerRepository

    @MockkBean
    protected lateinit var itemPriceServiceMock: ItemPriceService

    @MockkBean
    protected lateinit var userServiceMock: UserService

    protected val availableServers = listOf(
        ArcheageServer("ANY_EU_SERVER_1", 1),ArcheageServer("ANY_EU_SERVER_2", 2),
        ArcheageServer("ANY_CIS_SERVER_1", 3),ArcheageServer("ANY_CIS_SERVER_2", 4)
    )

    protected val numberOfCommonModelAttributes = 4

    @BeforeEach
    fun setUp() {
        every { archeageServerServiceMock.servers() } returns availableServers
    }

    fun ModelResultMatchersDsl.assertCommonModelAttributesForAllControllers(archeageServer: ArcheageServer){
        attribute("servers",availableServers)
        attribute("server",archeageServer)
        attributeExists("currentURL")
        attributeExists("timezone")
    }

}
