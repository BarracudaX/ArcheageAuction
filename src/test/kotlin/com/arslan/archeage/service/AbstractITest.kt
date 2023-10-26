package com.arslan.archeage.service

import com.arslan.archeage.AbstractTest
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails
import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
abstract class AbstractITest : AbstractTest(){

    companion object{

        @Container
        @ServiceConnection(type = [JdbcConnectionDetails::class])
        val mysql = MySQLContainer("mysql:latest").withInitScript("test.sql")

    }

}