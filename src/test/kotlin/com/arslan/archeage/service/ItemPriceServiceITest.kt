package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.User
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ItemPriceServiceITest(private val itemPriceService: ItemPriceService) : AbstractITest() {

    private lateinit var someItem: PurchasableItem
    private lateinit var anotherItem: PurchasableItem
    private lateinit var currentUserArcheageServer: ArcheageServer
    private lateinit var anotherArcheageServer: ArcheageServer
    private lateinit var someUser: User
    private lateinit var anotherUser: User

    @BeforeEach
    fun setUp() {
        currentUserArcheageServer = archeageServerRepository.save(ArcheageServer("SOME_SERVER"))
        anotherArcheageServer = archeageServerRepository.save(ArcheageServer("SOME_SERVER_1"))
        someItem = itemRepository.save(PurchasableItem("ANY_ITEM_NAME_1","ANY_ITEM_DESCRIPTION_1",currentUserArcheageServer))
        anotherItem = itemRepository.save(PurchasableItem("ANY_ITEM_NAME_2","ANY_ITEM_DESCRIPTION_2",currentUserArcheageServer))
        someUser = userRepository.save(User("ANY_EMAIL","ANY_PASSWORD"))
        anotherUser = userRepository.save(User("ANOTHER_EMAIL","ANY_PASSWORD"))
        ArcheageServerContextHolder.setServerContext(currentUserArcheageServer)
    }

    @Test
    fun `should return empty list when requesting latest prices for item that do not have any price specified`() {
        itemPriceService.latestPrices(listOf(someItem,anotherItem),currentUserArcheageServer).shouldBeEmpty()
    }

    @Test
    fun `should return latest prices for items`() {
        userPriceRepository.save(UserPrice(someItem,currentUserArcheageServer,Price(1,1,1),someUser))
        val lastPrice = userPriceRepository.save(UserPrice(someItem,currentUserArcheageServer,Price(1,1,1),someUser))

        val result = itemPriceService.latestPrices(listOf(someItem,anotherItem),currentUserArcheageServer)

        result.shouldHaveSize(1)
        result[0] shouldBe lastPrice
    }

    @Test
    fun `should return latest prices for items that belong to the current user's archeage server`() {
        val expectedPrice = userPriceRepository.save(UserPrice(someItem,currentUserArcheageServer,Price(1,1,1),someUser))
        userPriceRepository.save(UserPrice(someItem,anotherArcheageServer,Price(1,1,1),someUser))

        val result = itemPriceService.latestPrices(listOf(someItem),currentUserArcheageServer)

        result.shouldHaveSize(1)
        result.shouldContain(expectedPrice)
    }

    @Test
    fun `should return empty list when requesting latest prices of specific user that have not specified any`() {
        userPriceRepository.save(UserPrice(someItem,currentUserArcheageServer,Price(1,1,1),someUser))
        userPriceRepository.save(UserPrice(anotherItem,currentUserArcheageServer,Price(1,1,1),someUser))

        itemPriceService.userPrices(listOf(someItem,anotherItem),anotherUser.id!!,currentUserArcheageServer).shouldBeEmpty()
    }

    @Test
    fun `should return latest user prices for items for which user have specified them`() {
        userPriceRepository.save(UserPrice(someItem,currentUserArcheageServer,Price(1,1,1),someUser))
        userPriceRepository.save(UserPrice(anotherItem,currentUserArcheageServer,Price(1,1,1),someUser))
        userPriceRepository.save(UserPrice(anotherItem,currentUserArcheageServer,Price(1,1,1),anotherUser))
        val expectedPrice = userPriceRepository.save(UserPrice(anotherItem,currentUserArcheageServer,Price(1,1,1),anotherUser))

        val result = itemPriceService.userPrices(listOf(someItem,anotherItem),anotherUser.id!!,currentUserArcheageServer)

        result.shouldHaveSize(1)
        result.shouldHaveKey(anotherItem.id!!)
        result[anotherItem.id].shouldBe(expectedPrice)
    }

    @Test
    fun `should not return user price that is defined for different archeage server`() {
        val expectedPrice = userPriceRepository.save(UserPrice(someItem,currentUserArcheageServer,Price(1,1,1),someUser))
        userPriceRepository.save(UserPrice(someItem,anotherArcheageServer,Price(2,2,2),someUser))

        val result = itemPriceService.userPrices(listOf(someItem),someUser.id!!,currentUserArcheageServer)

        result.shouldHaveSize(1)
        result[someItem.id!!].shouldBe(expectedPrice)
    }

    @Test
    fun `should return empty result when requesting user prices of user that does not exist`() {
        val idOfNonExistingUser = 12093L
        userRepository.existsById(idOfNonExistingUser).shouldBeFalse()

        itemPriceService.userPrices(listOf(someItem),idOfNonExistingUser,currentUserArcheageServer).shouldBeEmpty()
    }
}