package com.arslan.archeage.service

import com.arslan.archeage.AbstractTest
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.repository.ItemPriceRepository
import com.arslan.archeage.repository.LocationRepository
import com.arslan.archeage.repository.PackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractITest : AbstractTest(){

    @Autowired
    protected lateinit var packRepository: PackRepository

    @Autowired
    protected lateinit var itemPriceRepository: ItemPriceRepository

    @Autowired
    protected lateinit var locationRepository: LocationRepository

    @Autowired
    protected lateinit var archeageServerRepository: ArcheageServerRepository

    companion object{

        @JvmStatic
        val mysql = MySQLContainer("mysql:latest")
            .withInitScript("test.sql")
            .withReuse(true)

        @DynamicPropertySource
        @JvmStatic
        fun dynamicProperties(registry: DynamicPropertyRegistry){
            mysql.start()
            registry.add("spring.datasource.url"){ mysql.jdbcUrl }
            registry.add("spring.datasource.username"){ mysql.username }
            registry.add("spring.datasource.password"){ mysql.password }
        }
    }

}