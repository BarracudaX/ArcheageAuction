package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

class ItemServiceITest(private val itemService: ItemService) : AbstractITest() {

    private lateinit var archeageServer: ArcheageServer

    @BeforeEach
    fun setUp(){
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY"))
    }

    @Test
    fun `should return empty page if there are no items`() {
        itemService.purchasableItems(Pageable.unpaged(),archeageServer).isEmpty.shouldBeTrue()
    }

    @Test
    fun `should return empty page if there are not purchasable items`() {
        itemRepository.save(Item("NOT_PURCHASABLE_ITEM","ANY",archeageServer))
        val packLocation = locationRepository.save(Location("ANY",Continent.WEST,archeageServer,true))
        packRepository.save(Pack(packLocation, PackPrice(Price(1,2,3),packLocation),10,"PACK","ANY"))

        itemService.purchasableItems(Pageable.unpaged(),archeageServer).isEmpty.shouldBeTrue()
    }

    @Test
    fun `should return purchasable items`() {
        val expected = listOf(
            purchasableItemRepository.save(PurchasableItem("PURCHASABLE_1","ANY",archeageServer)),
            purchasableItemRepository.save(PurchasableItem("PURCHASABLE_2","ANY",archeageServer)),
            purchasableItemRepository.save(PurchasableItem("PURCHASABLE_3","ANY",archeageServer)),
            purchasableItemRepository.save(PurchasableItem("PURCHASABLE_4","ANY",archeageServer)),
        )

        assertSoftly(itemService.purchasableItems(PageRequest.of(1,1, Sort.by("id")),archeageServer)) {
            totalElements shouldBe 4
            totalPages shouldBe 4
            content.shouldContainExactly(expected[1])
            hasNext().shouldBeTrue()
            hasPrevious().shouldBeTrue()
        }
        assertSoftly(itemService.purchasableItems(Pageable.unpaged(),archeageServer)) {
            totalElements shouldBe 4
            totalPages shouldBe 1
            content.shouldContainExactlyInAnyOrder(expected)
            hasNext().shouldBeFalse()
            hasPrevious().shouldBeFalse()
        }
        assertSoftly(itemService.purchasableItems(PageRequest.of(1,2, Sort.by("id")),archeageServer)) {
            totalElements shouldBe 4
            totalPages shouldBe 2
            content.shouldContainExactly(expected.subList(2,4))
            hasNext().shouldBeFalse()
            hasPrevious().shouldBeTrue()
        }
        assertSoftly(itemService.purchasableItems(PageRequest.of(0,2, Sort.by("id")),archeageServer)) {
            totalElements shouldBe 4
            totalPages shouldBe 2
            content.shouldContainExactly(expected.subList(0,2))
            hasNext().shouldBeTrue()
            hasPrevious().shouldBeFalse()
        }
    }
}