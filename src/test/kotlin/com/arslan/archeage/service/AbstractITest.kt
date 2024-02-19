package com.arslan.archeage.service

import com.arslan.archeage.*
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.entity.pack.PackProfit
import com.arslan.archeage.entity.pack.PackProfitKey
import com.arslan.archeage.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import org.springframework.test.jdbc.JdbcTestUtils
import org.springframework.transaction.annotation.Transactional

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

    fun createPack(archeageServer: ArcheageServer, continent: Continent) : PackDTO {
        val category = categoryRepository.save(Category("SOME_CATEGORY",null,archeageServer))
        val createLocation = locationRepository.save(Location("SOME_CREATE_LOCATION",continent,archeageServer,true))
        val sellLocation = locationRepository.save(Location("SOME_SELL_LOCATION",continent,archeageServer,true))
        val purchasableItem = purchasableItemRepository.save(PurchasableItem("SOME_PURCHASABLE_ITEM","SOME_DESCRIPTION",archeageServer))
        val user = userRepository.findByEmail("SOME_EMAIL") ?: userRepository.save(User("SOME_EMAIL","ANY"))
        val userPrice = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableItem),randomPrice()))

        val pack = packRepository.save(Pack(createLocation, PackPrice(randomPrice(),sellLocation),4,category,"SOME_PACK","SOME_DESC").apply { addMaterial(CraftingMaterial(5,purchasableItem)) })
        val packProfit = packProfitRepository.save(PackProfit(PackProfitKey(pack,user),randomPrice()))


        return pack.toDTO(mapOf(purchasableItem.id!! to userPrice),packProfit.percentage)
    }

    fun createPack(destinationLocation: Location,archeageServer: ArcheageServer,departureLocation: Location,continent: Continent) : PackDTO{
        val category = categoryRepository.save(Category("SOME_CATEGORY",null,archeageServer))
        val purchasableItem = purchasableItemRepository.save(PurchasableItem("SOME_PURCHASABLE_ITEM","SOME_DESCRIPTION",archeageServer))
        val user = userRepository.findByEmail("SOME_EMAIL") ?: userRepository.save(User("SOME_EMAIL","ANY"))
        val userPrice = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableItem),randomPrice()))

        val pack = packRepository.save(Pack(departureLocation, PackPrice(randomPrice(),destinationLocation),4,category,"SOME_PACK","SOME_DESC").apply { addMaterial(CraftingMaterial(5,purchasableItem)) })
        val packProfit = packProfitRepository.save(PackProfit(PackProfitKey(pack,user),randomPrice()))

        return pack.toDTO(mapOf(purchasableItem.id!! to userPrice),packProfit.percentage)
    }

    fun createPackWithDepartureLocation(archeageServer: ArcheageServer, departureLocation: Location) : PackDTO{
        val category = categoryRepository.save(Category("SOME_CATEGORY",null,archeageServer))
        val sellLocation = locationRepository.save(Location("SOME_SELL_LOCATION",departureLocation.continent,archeageServer,true))
        val purchasableItem = purchasableItemRepository.save(PurchasableItem("SOME_PURCHASABLE_ITEM","SOME_DESCRIPTION",archeageServer))
        val user = userRepository.findByEmail("SOME_EMAIL") ?: userRepository.save(User("SOME_EMAIL","ANY"))
        val userPrice = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableItem),randomPrice()))

        val pack = packRepository.save(Pack(departureLocation, PackPrice(randomPrice(),sellLocation),4,category,"SOME_PACK","SOME_DESC").apply { addMaterial(CraftingMaterial(5,purchasableItem)) })
        val packProfit = packProfitRepository.save(PackProfit(PackProfitKey(pack,user),randomPrice()))


        return pack.toDTO(mapOf(purchasableItem.id!! to userPrice),packProfit.percentage)
    }

    fun createPackWithDestinationLocation(archeageServer: ArcheageServer,destinationLocation: Location) : PackDTO{
        val category = categoryRepository.save(Category("SOME_CATEGORY",null,archeageServer))
        val departureLocation = locationRepository.save(Location("SOME_SELL_LOCATION",destinationLocation.continent,archeageServer,true))
        val purchasableItem = purchasableItemRepository.save(PurchasableItem("SOME_PURCHASABLE_ITEM","SOME_DESCRIPTION",archeageServer))
        val user = userRepository.findByEmail("SOME_EMAIL") ?: userRepository.save(User("SOME_EMAIL","ANY"))
        val userPrice = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableItem),randomPrice()))

        val pack = packRepository.save(Pack(departureLocation, PackPrice(randomPrice(),destinationLocation),4,category,"SOME_PACK","SOME_DESC").apply { addMaterial(CraftingMaterial(5,purchasableItem)) })
        val packProfit = packProfitRepository.save(PackProfit(PackProfitKey(pack,user),randomPrice()))

        return pack.toDTO(mapOf(purchasableItem.id!! to userPrice),packProfit.percentage)
    }

    fun createPackWithCategory(archeageServer: ArcheageServer,category: Category,continent: Continent) : PackDTO{
        val createLocation = locationRepository.save(Location("SOME_CREATE_LOCATION",continent,archeageServer,true))
        val sellLocation = locationRepository.save(Location("SOME_SELL_LOCATION",continent,archeageServer,true))
        val purchasableItem = purchasableItemRepository.save(PurchasableItem("SOME_PURCHASABLE_ITEM","SOME_DESCRIPTION",archeageServer))
        val user = userRepository.findByEmail("SOME_EMAIL") ?: userRepository.save(User("SOME_EMAIL","ANY"))
        val userPrice = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableItem),randomPrice()))

        val pack = packRepository.save(Pack(createLocation, PackPrice(randomPrice(),sellLocation),4,category,"SOME_PACK","SOME_DESC").apply { addMaterial(CraftingMaterial(5,purchasableItem)) })
        val packProfit = packProfitRepository.save(PackProfit(PackProfitKey(pack,user),randomPrice()))

        return pack.toDTO(mapOf(purchasableItem.id!! to userPrice),packProfit.percentage)
    }

    fun packsThatBelongToCategories(packService: PackService,continent: Continent,archeageServer: ArcheageServer,categories: List<Category>) : List<PackDTO> =
        packService.packs(PackRequest(continent,null,null,null,categories.mapNotNull(Category::id)), Pageable.unpaged(),archeageServer).content

    fun packsWithDepartureLocation(packService: PackService,continent: Continent,archeageServer: ArcheageServer,departureLocation: Location) : List<PackDTO> =
        packService.packs(PackRequest(continent,departureLocation.id!!), Pageable.unpaged(),archeageServer).content

    fun packsWithDestinationLocation(packService: PackService,continent: Continent,archeageServer: ArcheageServer,destinationLocation: Location) : List<PackDTO> =
        packService.packs(PackRequest(continent,null,destinationLocation.id!!), Pageable.unpaged(),archeageServer).content

    fun continentPacks(packService: PackService,continent: Continent,archeageServer: ArcheageServer) : List<PackDTO> = packService.packs(PackRequest(continent), Pageable.unpaged(),archeageServer).content

    fun packs(departureLocation: Location,packService: PackService,continent: Continent,archeageServer: ArcheageServer,destinationLocation: Location) : List<PackDTO> =
        packService.packs(PackRequest(continent,departureLocation.id!!,destinationLocation.id!!), Pageable.unpaged(),archeageServer).content

}