package com.arslan.archeage.selenium

import com.arslan.archeage.*
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.entity.pack.PackProfit
import com.arslan.archeage.entity.pack.PackProfitKey
import com.arslan.archeage.event.ItemPriceChangeEvent
import com.arslan.archeage.repository.*
import com.arslan.archeage.service.PackProfitService
import com.arslan.archeage.service.PackService
import com.arslan.archeage.service.UserService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
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
import org.springframework.retry.backoff.BackOffPolicy
import org.springframework.retry.backoff.BackOffPolicyBuilder
import org.springframework.retry.support.RetryTemplate
import org.springframework.retry.support.RetryTemplateBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.Duration

@ActiveProfiles("test","headless")
@Import(SeleniumTest.SeleniumConfiguration::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class SeleniumTest : AbstractTestContainerTest() {

    @Autowired
    protected lateinit var retryTemplate: RetryTemplate

    @Autowired
    protected lateinit var locationRepository: LocationRepository

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

    @Autowired
    protected lateinit var packProfitRepository: PackProfitRepository

    @Autowired
    protected lateinit var userPriceRepository: UserPriceRepository

    @Autowired
    protected lateinit var packService: PackService

    @Autowired
    protected lateinit var purchasableItemRepository: PurchasableItemRepository

    @Autowired
    protected lateinit var categoryRepository: CategoryRepository

    @Autowired
    protected lateinit var packProfitService: PackProfitService

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
            manage().timeouts().scriptTimeout(Duration.ofMinutes(5))
        }

        @Bean
        fun retryTemplate() : RetryTemplate = RetryTemplateBuilder()
            .retryOn(StaleElementReferenceException::class.java)
            .retryOn(NoSuchElementException::class.java)
            .customBackoff(BackOffPolicyBuilder.newBuilder().delay(500).build())
            .maxAttempts(5)
            .build()
    }

    @BeforeEach
    fun setUp(){
        clearDB(jdbcTemplate)
    }

    fun createArcheageServer(name: String) : ArcheageServer = archeageServerRepository.save(ArcheageServer(name))

    fun createWestDepartureLocation(name: String, archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.WEST,archeageServer))

    fun createEastDepartureLocation(name: String,archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.EAST,archeageServer))

    fun createWestDestinationLocation(name: String,archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.WEST,archeageServer,true))
    fun createEastDestinationLocation(name: String,archeageServer: ArcheageServer) = locationRepository.save(Location(name,Continent.EAST,archeageServer,true))

    fun createPack(name: String, createLocation: Location, sellLocation: Location, price: Price, quantity: Int, category: Category, materials: List<Triple<String,Price,Int>>,workingPoints: Int = 10,user: User) : Pair<Pack,PackDTO>{
        val pack = Pack(createLocation,PackPrice(price,sellLocation),quantity,category,workingPoints,name,"ANY_DESC")
        val items = materials.map { (name,price,quantity) ->
            val item = purchasableItemRepository.save(PurchasableItem(name, "ANY", sellLocation.archeageServer))
            pack.addMaterial(CraftingMaterial(quantity, item))
            item to price
        }

        packRepository.save(pack)

        val userPrices = items.associate { (item,price) ->
            val userPrice = userPriceRepository.save(UserPrice(UserPriceKey(user, item), price))
            pack.addMaterial(CraftingMaterial(quantity, item))
            packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,item,user,price))
            item.id!! to userPrice
        }


        return pack to pack.toDTO(userPrices,100,false)
    }

    fun createCategory(name: String,archeageServer: ArcheageServer) : Category{
        return categoryRepository.save(Category(name,null,archeageServer))
    }

    fun createUser(email: String,password: String) = userRepository.save(User(email,passwordEncoder.encode(password)))

}