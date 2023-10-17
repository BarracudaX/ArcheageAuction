package com.arslan.data.config

import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextClosedEvent
import java.nio.file.Files
import kotlin.io.path.Path

@Configuration
class InfrastructureConfiguration {

    @Profile("schema")
    @Bean
    fun schemaCleaner() : ApplicationListener<ContextClosedEvent> = ApplicationListener<ContextClosedEvent> {
        Files.deleteIfExists(Path("ArcheageAuctionData/create.sql"))
        Files.deleteIfExists(Path("ArcheageAuctionData/drop.sql"))
        Files.deleteIfExists(Path("create.sql"))
        Files.deleteIfExists(Path("drop.sql"))
    }
}