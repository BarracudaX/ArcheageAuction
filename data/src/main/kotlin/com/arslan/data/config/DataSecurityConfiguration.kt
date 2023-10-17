package com.arslan.data.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class DataSecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain = http.build()

}