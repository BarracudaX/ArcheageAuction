package com.arslan.archeage.selenium

import com.arslan.archeage.AbstractTestContainerTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.MessageSource
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class SeleniumTest : AbstractTestContainerTest() {

    @Autowired
    protected lateinit var messageSource: MessageSource

    @LocalServerPort
    protected var port: Int = -1

    protected lateinit var url: String

    protected val webDriver = ChromeDriver()

    @BeforeEach
    fun setUp(){
        url = "http://localhost:${port}/"
    }

    @AfterEach
    fun tearDown(){
        webDriver.quit()
    }
}