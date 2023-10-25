package com.arslan.archeage.controllers

import com.arslan.archeage.AbstractTest
import com.arslan.archeage.config.InfrastructureConfiguration
import com.arslan.archeage.config.SecurityConfiguration
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.service.ArcheageServerService
import com.arslan.archeage.service.LocationService
import com.arslan.archeage.service.PackService
import com.arslan.archeage.web.ArcheageServerChangeInterceptor
import com.arslan.archeage.web.ArcheageServerContextHandlerInterceptor
import com.arslan.archeage.web.ArcheageServerResolver
import com.ninjasquad.springmockk.MockkBean
import org.mockito.Mock
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationProvider


@Import(InfrastructureConfiguration::class,SecurityConfiguration::class)
@WebMvcTest
@ComponentScan(basePackages = ["com.arslan.archeage.web"])
abstract class AbstractControllerTest : AbstractTest() {

    @MockkBean
    private lateinit var authenticationProvider: AuthenticationProvider

    @MockkBean
    private lateinit var archeageServerService: ArcheageServerService

    @MockkBean
    private lateinit var locationService: LocationService

    @MockkBean
    private lateinit var packService: PackService

    @MockkBean
    private lateinit var archeageServerRepository: ArcheageServerRepository
}
