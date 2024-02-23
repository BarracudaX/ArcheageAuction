package com.arslan.archeage.selenium

import com.arslan.archeage.AbstractTestContainerTest
import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.User
import com.arslan.archeage.repository.ArcheageServerRepository
import com.arslan.archeage.repository.LocationRepository
import com.arslan.archeage.repository.PackRepository
import com.arslan.archeage.repository.UserRepository
import com.arslan.archeage.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test","headless")
@Import(SeleniumTest.SeleniumConfiguration::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class SeleniumTest : AbstractTestContainerTest() {

    @Autowired
    private lateinit var locationRepository: LocationRepository

    @Autowired
    protected lateinit var archeageServerRepository: ArcheageServerRepository

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

    @Autowired
    protected lateinit var packRepository: PackRepository

    @LocalServerPort
    protected var port: Int = -1

    @Autowired
    protected lateinit var webDriver: WebDriver

    @TestConfiguration
    class SeleniumConfiguration{

        @Profile("headless")
        @Bean(destroyMethod = "quit")
        fun headlessWebDriver() : WebDriver = ChromeDriver(ChromeOptions().apply {
            addArguments("--headless=new")
            addArguments("--window-size=1920,1080")
            addArguments("---start-maximized")
        })

        @Profile("!headless")
        @Bean(destroyMethod = "quit")
        fun nonHeadlessWebDriver() : WebDriver = ChromeDriver().apply {
            manage().window().maximize()
        }

    }

    @BeforeEach
    fun setUp(){
        clearDB(jdbcTemplate)
    }

    fun createArcheageServer() : ArcheageServer = archeageServerRepository.save(ArcheageServer("SOME_ARCHEAGE_SERVER"))

    fun createWestDepartureLocation(name: String, archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.WEST,archeageServer))

    fun createEastDepartureLocation(name: String,archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.EAST,archeageServer))

    fun createWestDestinationLocation(name: String,archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.WEST,archeageServer,true))
    fun createEastDestinationLocation(name: String,archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.EAST,archeageServer,true))

    fun createUser(email: String,password: String) = userRepository.save(User(email,passwordEncoder.encode(password)))

}