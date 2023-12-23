package com.arslan.archeage.service

import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.User
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
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
    private lateinit var someUser: User
    private lateinit var anotherUser: User

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
    fun `should return empty list when requesting latest prices for item that do not have any price specified`() {
        itemPriceService.latestPrices(listOf(someItem,anotherItem),currentUserArcheageServer).shouldBeEmpty()
    }

    @Test
    fun `should return latest prices for items`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        val lastPrice = userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))

        val result = itemPriceService.latestPrices(listOf(someItem,anotherItem),currentUserArcheageServer)

        result.shouldHaveSize(1)
        result[0] shouldBe lastPrice
    }

    @Test
    fun `should return empty list when requesting latest prices of specific user that have not specified any`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem), Price(1,1,1)))

        itemPriceService.userPrices(listOf(someItem,anotherItem),anotherUser.id!!,currentUserArcheageServer).shouldBeEmpty()
    }

    @Test
    fun `should return latest user prices for items for which user have specified them`() {
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,someItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(someUser,anotherItem), Price(1,1,1)))
        userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,anotherItem), Price(1,1,1)))
        val expectedPrice = userPriceRepository.save(UserPrice(UserPriceKey(anotherUser,anotherItem), Price(1,1,1)))

        val result = itemPriceService.userPrices(listOf(someItem,anotherItem),anotherUser.id!!,currentUserArcheageServer)

        result.shouldHaveSize(1)
        result.shouldHaveKey(anotherItem.id!!)
        result[anotherItem.id].shouldBe(expectedPrice)
    }


    @Test
    fun `should return empty result when requesting user prices of user that does not exist`() {
        val idOfNonExistingUser = 12093L
        userRepository.existsById(idOfNonExistingUser).shouldBeFalse()

        itemPriceService.userPrices(listOf(someItem),idOfNonExistingUser,currentUserArcheageServer).shouldBeEmpty()
    }
}