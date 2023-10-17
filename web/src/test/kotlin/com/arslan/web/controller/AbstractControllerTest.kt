package com.arslan.web.controller

import com.arslan.web.AbstractTest
import com.arslan.web.config.InfrastructureConfiguration
import com.arslan.web.config.SecurityConfiguration
import com.ninjasquad.springmockk.MockkBean
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.AuthenticationProvider


@Import(InfrastructureConfiguration::class,SecurityConfiguration::class)
@WebMvcTest
abstract class AbstractControllerTest : AbstractTest() {

    @MockkBean
    private lateinit var authenticationProvider: AuthenticationProvider

}