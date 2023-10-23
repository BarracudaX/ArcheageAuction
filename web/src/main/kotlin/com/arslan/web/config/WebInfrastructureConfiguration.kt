package com.arslan.web.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.function.client.WebClient
import java.nio.file.Files
import kotlin.io.path.Path

@Configuration
class WebInfrastructureConfiguration {

    @Bean
    fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()

    @Profile("schema")
    @Bean
    fun schemaCleaner() : ApplicationListener<ContextClosedEvent> = ApplicationListener<ContextClosedEvent> {
        Files.deleteIfExists(Path("web-create.sql"))
        Files.deleteIfExists(Path("web-drop.sql"))
    }

    @Bean
    fun dataWebClient(@Value("\${data.app.base.url}") dataAppBaseUrl: String) : WebClient = WebClient.create(dataAppBaseUrl)

    @Bean
    fun defaultWebClient() : WebClient = WebClient.create()

}