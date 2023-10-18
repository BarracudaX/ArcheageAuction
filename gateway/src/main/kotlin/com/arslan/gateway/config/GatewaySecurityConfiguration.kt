package com.arslan.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class GatewaySecurityConfiguration {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity) : SecurityWebFilterChain = http
        .authorizeExchange { authorize ->
            authorize.anyExchange().permitAll()
        }.build()

}