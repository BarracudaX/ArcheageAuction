package com.arslan.archeage.service

import com.arslan.archeage.AbstractTest
import com.arslan.archeage.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestEntityManager
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractITest : AbstractTest(){

    @Autowired
    protected lateinit var packRepository: PackRepository

    @Autowired
    protected lateinit var userPriceRepository: UserPriceRepository

    @Autowired
    protected lateinit var locationRepository: LocationRepository

    @Autowired
    protected lateinit var archeageServerRepository: ArcheageServerRepository

    @Autowired
    protected lateinit var itemRecipeRepository: ItemRecipeRepository

    @Autowired
    protected lateinit var packRecipeRepository: PackRecipeRepository

    @Autowired
    protected lateinit var itemRepository: ItemRepository

    @Autowired
    protected lateinit var purchasableItemRepository: PurchasableItemRepository

    @Autowired
    protected lateinit var testEntityManager: TestEntityManager

    @Autowired
    protected lateinit var userRepository: UserRepository


    companion object{

        @JvmStatic
        val mysql = MySQLContainer("mysql:latest").withReuse(true)

        @DynamicPropertySource
        @JvmStatic
        fun dynamicProperties(registry: DynamicPropertyRegistry){
            mysql.start()
            registry.add("spring.datasource.url"){ mysql.jdbcUrl }
            registry.add("spring.datasource.username"){ mysql.username }
            registry.add("spring.datasource.password"){ mysql.password }
            registry.add("spring.datasource.url"){ mysql.jdbcUrl }
            registry.add("spring.liquibase.user"){ mysql.username }
            registry.add("spring.liquibase.password"){ mysql.password }
            registry.add("spring.liquibase.url"){ mysql.jdbcUrl }
        }
    }

}