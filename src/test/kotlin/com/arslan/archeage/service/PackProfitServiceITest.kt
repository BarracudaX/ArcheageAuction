package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.PackPercentageUpdate
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.entity.pack.PackProfitKey
import com.arslan.archeage.event.ItemPriceChangeEvent
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PackProfitServiceITest(private val packProfitService: PackProfitService) : AbstractITest() {

    private val nonPurchasableMaterials = mutableListOf<Item>()
    private val purchasableMaterials = mutableListOf<PurchasableItem>()
    private lateinit var pack: Pack
    private lateinit var user: User
    private lateinit var anotherUser: User
    private lateinit var archeageServer: ArcheageServer
    private val prices = mutableMapOf<Long,Price>()
    private lateinit var packPercentageUpdate: PackPercentageUpdate

    @BeforeEach
    fun setUp(){
        clearDB(jdbcTemplate)
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY"))
        user = userRepository.save(User("ANY_USER_EMAIL","ANY_USER_PASSWORD"))
        anotherUser = userRepository.save(User("ANY_USER_EMAIL_2","ANY_USER_PASSWORD_2"))

        nonPurchasableMaterials.add(itemRepository.save(Item("ITEM_1","ANY",archeageServer)))
        nonPurchasableMaterials.add(itemRepository.save(Item("ITEM_2","ANY",archeageServer)))

        val purchasable1 = itemRepository.save(PurchasableItem("PURCHASABLE_1","ANY",archeageServer))
        val purchasable2 = itemRepository.save(PurchasableItem("PURCHASABLE_2","ANY",archeageServer))
        prices[purchasable1.id!!] = Price.of(5,0,0)
        prices[purchasable2.id!!] = Price.of(10,0,0)
        purchasableMaterials.add(purchasable1)
        purchasableMaterials.add(purchasable2)

        val location = locationRepository.save(Location("ANY_LOCATION",Continent.WEST,archeageServer,true))
        val category = categoryRepository.save(Category("ANY_CATEGORY",null,archeageServer))
        pack = packRepository.save(Pack(location,PackPrice(Price(100,0,0),location),5,category,10,"PACK","ANY"))
        nonPurchasableMaterials.forEach { material -> pack.addMaterial(CraftingMaterial(10,material)) }
        pack.addMaterial(CraftingMaterial(2,purchasable1))
        pack.addMaterial(CraftingMaterial(4,purchasable2))
        pack = packRepository.save(pack)

        packPercentageUpdate = PackPercentageUpdate(pack.id!!,110,user.id!!)
    }

    @Test
    fun `should do nothing when receiving item price change event for non existing item`() {
        val nonExistingItem = PurchasableItem("NON_EXISTING_ITEM","ANY",archeageServer).apply { id = 32103210321L }
        itemRepository.existsById(nonExistingItem.id!!).shouldBeFalse()
        val event = ItemPriceChangeEvent(this, nonExistingItem,user, Price(2,3,4))

        packProfitService.onItemPriceChange(event)

        packProfitRepository.findAll().shouldBeEmpty()
    }

    @Test
    fun `should not create profit for pack when receiving item price change event and not all pack purchasable materials have price`() {
        val purchasablePrice = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableMaterials[0]),Price(2,3,4)))
        packProfitRepository.findAll().shouldBeEmpty()

        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice.id.purchasableItem,purchasablePrice.id.user,purchasablePrice.price))

        packProfitRepository.findAll().shouldBeEmpty()
    }

    /**
     * This test case covers a case where pack has,for example,2 purchasable materials and for each material separate users have specified price,
     * but none of the users have specified prices for all purchasable materials.
     */
    @Test
    fun `should not create profit for pack when receiving item price change event and pack purchasable materials have price from different users`() {
        val purchasablePrice = userPriceRepository.save(UserPrice(UserPriceKey(user,purchasableMaterials[0]),Price(2,3,4)))
        val purchasablePrice2 = userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,purchasableMaterials[1]),Price(2,3,4)))
        packProfitRepository.findAll().shouldBeEmpty()

        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice.id.purchasableItem,purchasablePrice.id.user,purchasablePrice.price))
        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice2.id.purchasableItem,purchasablePrice2.id.user,purchasablePrice2.price))

        packProfitRepository.findAll().shouldBeEmpty()
    }

    @Test
    fun `should create profit for a pack when user have specified prices for all purchasable materials and profit does not exist`() {
        val purchasablePrice = userPriceRepository.saveAndFlush(UserPrice(UserPriceKey(user,purchasableMaterials[0]),prices[purchasableMaterials[0].id!!]!!))
        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice.id.purchasableItem,purchasablePrice.id.user,purchasablePrice.price))
        packProfitRepository.findAll().shouldBeEmpty()

        val purchasablePrice2 = userPriceRepository.saveAndFlush(UserPrice(UserPriceKey(user,purchasableMaterials[1]),prices[purchasableMaterials[1].id!!]!!))

        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice2.id.purchasableItem,purchasablePrice2.id.user,purchasablePrice2.price))
        assertSoftly(packProfitRepository.findAll().shouldHaveSize(1).iterator().next()) {
            netProfit shouldBe Price.of(50,0,0)
            workingPointsProfit shouldBe Price.of(5,0,0)
            id.pack.id shouldBe pack.id
            id.user.id shouldBe user.id
        }
    }

    @Test
    fun `should update pack profit when item price decreases and pack profit exists`() {
        setUserPricesForTestPack()
        val decrease = Price(-5,0,0)
        userPriceRepository.saveAndFlush(UserPrice(UserPriceKey(user,purchasableMaterials[1]),prices[purchasableMaterials[1].id!!]!!.plus(decrease))) // instead of 10 gold, the material costs 5 gold

        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasableMaterials[1],user,decrease))

        assertSoftly(packProfitRepository.findAll().shouldHaveSize(1).iterator().next()) {
            netProfit shouldBe Price.of(30,0,0)
            workingPointsProfit shouldBe Price.of(3,0,0)
            id.pack.id shouldBe pack.id
            id.user.id shouldBe user.id
        }
    }

    @Test
    fun `should throw EmptyResultDataAccessException when trying to update pack percentage of not existing pack`() {
        val idOfNotExistingPack = 1203910L
        packRepository.existsById(idOfNotExistingPack).shouldBeFalse()

        shouldThrow<EmptyResultDataAccessException> { packProfitService.updatePercentage(packPercentageUpdate.copy(packID = idOfNotExistingPack)) }
    }

    @Test
    fun `should throw EmptyResultDataAccessException when trying to update pack percentage for not existing user`() {
        val idOfNotExistingUser = 12039102L
        userRepository.existsById(idOfNotExistingUser).shouldBeFalse()

        shouldThrow<EmptyResultDataAccessException> { packProfitService.updatePercentage(packPercentageUpdate.copy(userID = idOfNotExistingUser)) }
    }

    @Test
    fun `should throw EmptyResultDataAccessException when trying to update pack percentage for pack for which user has not specified prices for all materials`() {
        //user specifies price for one item and the second item's price is not set
        val purchasablePrice = userPriceRepository.saveAndFlush(UserPrice(UserPriceKey(user,purchasableMaterials[0]),prices[purchasableMaterials[0].id!!]!!))
        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice.id.purchasableItem,purchasablePrice.id.user,purchasablePrice.price))

        shouldThrow<EmptyResultDataAccessException> { packProfitService.updatePercentage(PackPercentageUpdate(pack.id!!,120,user.id!!)) }
    }

    @Test
    fun `should update pack profit percentage`() {
        setUserPricesForTestPack()
        packProfitRepository.findById(PackProfitKey(pack,user)).get().percentage shouldBe 100 //default value

        packProfitService.updatePercentage(packPercentageUpdate.copy(percentage = 120))

        assertSoftly(packProfitRepository.findById(PackProfitKey(pack,user)).get()) {
            percentage shouldBe 120
            netProfit shouldBe Price.of(60,0,0)
            workingPointsProfit shouldBe Price.of(6,0,0)
        }

    }

    private fun setUserPricesForTestPack(){
        val purchasablePrice = userPriceRepository.saveAndFlush(UserPrice(UserPriceKey(user,purchasableMaterials[0]),prices[purchasableMaterials[0].id!!]!!))
        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice.id.purchasableItem,purchasablePrice.id.user,purchasablePrice.price))
        val purchasablePrice2 = userPriceRepository.saveAndFlush(UserPrice(UserPriceKey(user,purchasableMaterials[1]),prices[purchasableMaterials[1].id!!]!!))
        packProfitService.onItemPriceChange(ItemPriceChangeEvent(this,purchasablePrice2.id.purchasableItem,purchasablePrice2.id.user,purchasablePrice2.price))

        packProfitRepository.existsById(PackProfitKey(pack,user)).shouldBeTrue()
    }
}