package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.User
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.event.ItemPriceChangeEvent
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents

@RecordApplicationEvents
class ItemPriceServiceITest(private val itemPriceService: ItemPriceService) : AbstractITest() {

    private lateinit var someItem: PurchasableItem
    private lateinit var anotherItem: PurchasableItem
    private lateinit var currentUserArcheageServer: ArcheageServer
    private lateinit var someUser: User
    private lateinit var anotherUser: User

    @Autowired
    private lateinit var events: ApplicationEvents

    @BeforeEach
    fun setUp() {
        currentUserArcheageServer = archeageServerRepository.save(ArcheageServer("SOME_SERVER"))
        someItem = itemRepository.save(PurchasableItem("ANY_ITEM_NAME_1","ANY_ITEM_DESCRIPTION_1",currentUserArcheageServer))
        anotherItem = itemRepository.save(PurchasableItem("ANY_ITEM_NAME_2","ANY_ITEM_DESCRIPTION_2",currentUserArcheageServer))
        someUser = userRepository.save(User("ANY_EMAIL","ANY_PASSWORD"))
        anotherUser = userRepository.save(User("ANOTHER_EMAIL","ANY_PASSWORD"))
        ArcheageServerContextHolder.setServerContext(currentUserArcheageServer)
    }

    @Test
    fun `should return empty list when requesting prices for item that do not have any price specified`() {
        itemPriceService.prices(listOf(someItem,anotherItem)).shouldBeEmpty()
    }

    @Test
    fun `should return latest prices for items`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        val lastPrice = userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,someItem), Price(2,2,2)))

        val result = itemPriceService.prices(listOf(someItem,anotherItem))

        result.shouldHaveSize(1)
        result[0] shouldBe lastPrice
    }

    @Test
    fun `should return empty list when requesting prices of specific user that have not specified any`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem), Price(1,1,1)))

        itemPriceService.userPrices(listOf(someItem,anotherItem),anotherUser.id!!).shouldBeEmpty()
    }

    @Test
    fun `should return latest user prices for items for which user have specified them`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,anotherItem), Price(1,1,1)))
        val expectedPrice = userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,anotherItem), Price(2,2,2)))

        val result = itemPriceService.userPrices(listOf(someItem,anotherItem),anotherUser.id!!)

        result.shouldHaveSize(1)
        result.shouldHaveKey(anotherItem.id!!)
        result[anotherItem.id].shouldBe(expectedPrice)
    }


    @Test
    fun `should return empty result when requesting user prices of user that does not exist`() {
        val idOfNonExistingUser = 12093L
        userRepository.existsById(idOfNonExistingUser).shouldBeFalse()

        itemPriceService.userPrices(listOf(someItem),idOfNonExistingUser).shouldBeEmpty()
    }

    @Test
    fun `should throw EmptyResultDataAccessException when trying to save user price for user that does not exist`() {
        val idOfNonExistingUser = 10030123L
        userRepository.existsById(idOfNonExistingUser).shouldBeFalse()

        shouldThrow<EmptyResultDataAccessException> { itemPriceService.saveUserPrice(UserPriceDTO(idOfNonExistingUser,someItem.id!!,Price(2,3,4))) }
    }

    @Test
    fun `should throw EmptyResultDataAccessException when trying to save user price for item that does not exist`() {
        val idOfNonExistingItem = 1203010L
        itemRepository.existsById(idOfNonExistingItem).shouldBeFalse()

        shouldThrow<EmptyResultDataAccessException> { itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,idOfNonExistingItem,Price(2,4,20))) }
    }

    @Test
    fun `should throw EmptyResultDataAccessException when trying to save user price for non-purchasable item`() {
        val nonPurchasableItem = itemRepository.save(Item("NON_PURCHASABLE_ITEM","ANY",currentUserArcheageServer))
        val location = locationRepository.save(Location("ANY_NAME",Continent.EAST,currentUserArcheageServer,true))
        val packItem = packRepository.save(Pack(location, PackPrice(Price(20,30,20),location),10,"PACK","ANY"))

        shouldThrow<EmptyResultDataAccessException> { itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,nonPurchasableItem.id!!,Price(20,23,4))) }
        shouldThrow<EmptyResultDataAccessException> { itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,packItem.id!!,Price(20,23,4))) }
    }

    @Test
    fun `should save user price and fire ItemPriceChangeEvent`() {
        itemPriceService.userPrices(listOf(someItem),someUser.id!!).shouldBeEmpty()

        itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,someItem.id!!,Price(15,20,20)))

        val result = itemPriceService.userPrices(listOf(someItem),someUser.id!!)

        assertSoftly(events.stream().toList().filterIsInstance<ItemPriceChangeEvent>()) {
            shouldHaveSize(1)
            assertSoftly(get(0)) {
                item shouldBe someItem
                user shouldBe someUser
                priceChange shouldBe Price(15,20,20)
            }
        }
        result.shouldHaveSize(1)
        assertSoftly(result[someItem.id]!!) {
            price shouldBe Price(15,20,20)
            id.user shouldBe someUser
            id.purchasableItem shouldBe someItem
        }
    }

    @Test
    fun `should update user price and fire ItemPriceChangeEvent`() {
        itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,someItem.id!!,Price(20,20,20)))
        val firstTimestamp = itemPriceService.userPrices(listOf(someItem),someUser.id!!)[someItem.id!!]!!.timestamp

        itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,someItem.id!!,Price(25,30,20)))

        val result = itemPriceService.userPrices(listOf(someItem),someUser.id!!)

        assertSoftly(events.stream().toList().filterIsInstance<ItemPriceChangeEvent>()) {
            shouldHaveSize(2)
            assertSoftly(get(0)) {
                item shouldBe someItem
                user shouldBe someUser
                priceChange shouldBe Price(20,20,20)
            }
            assertSoftly(get(1)) {
                item shouldBe someItem
                user shouldBe someUser
                priceChange shouldBe  Price(25,30,20) - Price(20,20,20)
            }
        }
        result.shouldHaveSize(1)
        assertSoftly(result[someItem.id]!!) {
            price shouldBe Price(25,30,20)
            timestamp shouldNotBe firstTimestamp
        }
    }

}