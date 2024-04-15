package com.arslan.archeage.config

import brave.handler.SpanHandler
import io.micrometer.tracing.exporter.SpanReporter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import zipkin2.reporter.brave.AsyncZipkinSpanHandler
import java.nio.file.Files
import java.security.SecureRandom
import java.util.concurrent.Executors
import kotlin.io.path.Path

@Configuration
class InfrastructureConfiguration {

    @Bean
    fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()

    @Profile("schema")
    @Bean
    fun schemaCleaner() : ApplicationListener<ContextClosedEvent> = ApplicationListener<ContextClosedEvent> {
        Files.deleteIfExists(Path("create.sql"))
        Files.deleteIfExists(Path("drop.sql"))
    }

    @Bean
    fun tomcatConnectorCustomizer() : TomcatConnectorCustomizer = TomcatConnectorCustomizer{
        it.protocolHandler.executor = Executors.newVirtualThreadPerTaskExecutor()
    }
}