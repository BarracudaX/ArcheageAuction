package com.arslan.archeage

import org.slf4j.LoggerFactory
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.elasticsearch.ElasticsearchContainer

abstract class AbstractTestContainerTest : AbstractTest(){

    companion object{

        private val containerLoggerConsumer = Slf4jLogConsumer(LoggerFactory.getLogger("Integration Test Containers"))

        @ServiceConnection
        @JvmStatic
        val mysql = MySQLContainer("mysql:8.1.0")
            .withReuse(true)

        @ServiceConnection
        @JvmStatic
        val elasticSearch = ElasticsearchContainer("elasticsearch:8.11.1")
            .withEnv("xpack.security.enabled","false")
            .withEnv("discovery.type","single-node")
            .withReuse(true)

        @DynamicPropertySource
        @JvmStatic
        fun dynamicProperties(registry: DynamicPropertyRegistry){
            mysql.start()
            elasticSearch.start()
            mysql.followOutput(containerLoggerConsumer)
            registry.add("spring.jpa.properties.hibernate.search.backend.hosts"){ elasticSearch.httpHostAddress }
        }
    }

}