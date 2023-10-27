package com.arslan.archeage.controllers

import com.arslan.archeage.AbstractTest
import com.arslan.archeage.config.InfrastructureConfiguration
import com.arslan.archeage.config.SecurityConfiguration
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.service.ArcheageServerService
import com.arslan.archeage.service.ItemPriceService
import com.arslan.archeage.service.LocationService
import com.arslan.archeage.service.PackService
import com.ninjasquad.springmockk.MockkBean
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationProvider


@Import(InfrastructureConfiguration::class,SecurityConfiguration::class)
@WebMvcTest
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
}
