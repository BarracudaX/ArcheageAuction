package com.arslan.price.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class PriceSecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain = http.build()

}