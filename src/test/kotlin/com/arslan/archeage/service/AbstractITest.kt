package com.arslan.archeage.service

import com.arslan.archeage.AbstractTest
import com.arslan.archeage.AbstractTestContainerTest
import com.arslan.archeage.repository.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestConstructor
import org.springframework.test.jdbc.JdbcTestUtils
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.elasticsearch.ElasticsearchContainer

@Transactional
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestEntityManager
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
abstract class AbstractITest : AbstractTestContainerTest() {

    @Autowired
    protected lateinit var categoryRepository: CategoryRepository

    @Autowired
    protected lateinit var packRepository: PackRepository

    @Autowired
    protected lateinit var userPriceRepository: UserPriceRepository

    @Autowired
    protected lateinit var locationRepository: LocationRepository

    @Autowired
    protected lateinit var archeageServerRepository: ArcheageServerRepository

    @Autowired
    protected lateinit var packProfitRepository: PackProfitRepository

    @Autowired
    protected lateinit var itemRepository: ItemRepository

    @Autowired
    protected lateinit var purchasableItemRepository: PurchasableItemRepository

    @Autowired
    protected lateinit var testEntityManager: TestEntityManager

    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    protected fun clearDB(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,"pack_profits","user_prices","pack_materials","packs","purchasable_items","items","locations","categories","archeage_servers","users")
    }

}