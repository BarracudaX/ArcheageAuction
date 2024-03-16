package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.UserPriceDTO
import com.arslan.archeage.entity.*
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
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents

@RecordApplicationEvents
class ItemPriceServiceITest(private val itemPriceService: ItemPriceService) : AbstractITest() {

    private lateinit var someItem: PurchasableItem
    private lateinit var anotherItem: PurchasableItem
    private lateinit var currentUserArcheageServer: ArcheageServer
    private lateinit var someUser: User
    private lateinit var anotherUser: User
    private lateinit var category: Category

    @Autowired
    private lateinit var events: ApplicationEvents

    @BeforeEach
    fun setUp() {
        currentUserArcheageServer = archeageServerRepository.save(ArcheageServer("SOME_SERVER"))
        someItem = itemRepository.save(PurchasableItem("ANY_ITEM_NAME_1","ANY_ITEM_DESCRIPTION_1",currentUserArcheageServer))
        anotherItem = itemRepository.save(PurchasableItem("ANY_ITEM_NAME_2","ANY_ITEM_DESCRIPTION_2",currentUserArcheageServer))
        someUser = userRepository.save(User("ANY_EMAIL","ANY_PASSWORD"))
        anotherUser = userRepository.save(User("ANOTHER_EMAIL","ANY_PASSWORD"))
        category = categoryRepository.save(Category("ANY_CATEGORY",null,currentUserArcheageServer))
        ArcheageServerContextHolder.setServerContext(currentUserArcheageServer)
    }

    @Test
    fun `should return empty list when requesting prices for items that do not have any price specified`() {
        itemPriceService.lastPrices(listOf(someItem,anotherItem)).shouldBeEmpty()
    }

    @Test
    fun `should return latest prices for items`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        val lastPrice = userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,someItem), Price(2,2,2)))

        val result = itemPriceService.lastPrices(listOf(someItem,anotherItem))

        result.shouldHaveSize(1)
        result[0] shouldBe lastPrice
    }

    @Test
    fun `should return empty list when requesting prices of specific user that have not specified any price`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem), Price(1,1,1)))

        itemPriceService.userItemPrices(listOf(someItem,anotherItem),anotherUser.id!!).shouldBeEmpty()
    }

    @Test
    fun `should return latest user prices for items for which user have specified them`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,anotherItem), Price(1,1,1)))
        val expectedPrice = userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,anotherItem), Price(2,2,2)))

        val result = itemPriceService.userItemPrices(listOf(someItem,anotherItem),anotherUser.id!!)

        result.shouldHaveSize(1)
        result.shouldHaveKey(anotherItem.id!!)
        result[anotherItem.id].shouldBe(expectedPrice)
    }


    @Test
    fun `should return empty result when requesting user prices of user that does not exist`() {
        val idOfNonExistingUser = 12093L
        userRepository.existsById(idOfNonExistingUser).shouldBeFalse()

        itemPriceService.userItemPrices(listOf(someItem),idOfNonExistingUser).shouldBeEmpty()
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
        val packItem = packRepository.save(Pack(location, PackPrice(Price(20,30,20),location),10,category,10,"PACK","ANY"))

        shouldThrow<EmptyResultDataAccessException> { itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,nonPurchasableItem.id!!,Price(20,23,4))) }
        shouldThrow<EmptyResultDataAccessException> { itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,packItem.id!!,Price(20,23,4))) }
    }

    @Test
    fun `should save user price and fire ItemPriceChangeEvent`() {
        itemPriceService.userItemPrices(listOf(someItem),someUser.id!!).shouldBeEmpty()

        itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,someItem.id!!,Price(15,20,20)))

        val result = itemPriceService.userItemPrices(listOf(someItem),someUser.id!!)

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
        val firstTimestamp = itemPriceService.userItemPrices(listOf(someItem),someUser.id!!)[someItem.id!!]!!.timestamp

        itemPriceService.saveUserPrice(UserPriceDTO(someUser.id!!,someItem.id!!,Price(25,30,20)))

        val result = itemPriceService.userItemPrices(listOf(someItem),someUser.id!!)

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

    @Test
    fun `should return empty page when requesting user prices of non existing user`() {
        val idOfNonExistingUser = 100231051L
        userRepository.existsById(idOfNonExistingUser).shouldBeFalse()

        itemPriceService.userPrices(idOfNonExistingUser,currentUserArcheageServer, Pageable.unpaged()).isEmpty.shouldBeTrue()
    }

    @Test
    fun `should return empty page when requesting user prices of user that have not provided any prices`() {
        itemPriceService.userPrices(someUser.id!!,currentUserArcheageServer, Pageable.unpaged()).isEmpty.shouldBeTrue()
    }

    @Test
    fun `should return user prices`() {
        val expectedPrices = listOf(
            userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem),Price(20,30,40))),
            userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem),Price(10,10,20)))
        )

        assertSoftly(itemPriceService.userPrices(someUser.id!!,currentUserArcheageServer, Pageable.unpaged())) {
            hasNext() shouldBe false
            hasPrevious() shouldBe false
            totalPages shouldBe 1
            totalElements shouldBe 2
            content shouldContainExactlyInAnyOrder expectedPrices
        }
    }

    @Test
    fun `should return user prices(2)`() {
        val expectedPrices = listOf(
            userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem),Price(20,30,40))),
            userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem),Price(10,10,20)))
        )

        assertSoftly(itemPriceService.userPrices(someUser.id!!,currentUserArcheageServer, PageRequest.of(0,1, Sort.by("id.purchasableItem.id")))){
            hasNext().shouldBeTrue()
            hasPrevious().shouldBeFalse()
            content.shouldContainExactly(expectedPrices[0])
        }
    }
}