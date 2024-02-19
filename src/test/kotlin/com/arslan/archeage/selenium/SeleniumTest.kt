package com.arslan.archeage.selenium

import com.arslan.archeage.AbstractTestContainerTest
import com.arslan.archeage.controllers.RegistrationForm
import com.arslan.archeage.repository.UserRepository
import com.arslan.archeage.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.MessageSource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class SeleniumTest : AbstractTestContainerTest() {

    @Autowired
    protected lateinit var messageSource: MessageSource

    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    protected lateinit var userService: UserService

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var passwordEncoder: PasswordEncoder

    @LocalServerPort
    protected var port: Int = -1

    protected val webDriver = ChromeDriver()

    @BeforeEach
    fun setUp(){
        clearDB(jdbcTemplate)
    }

    @AfterEach
    fun tearDown(){
        webDriver.quit()
    }

}