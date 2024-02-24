package com.arslan.archeage.unit

import com.arslan.archeage.Continent
import com.arslan.archeage.PackDTO
import com.arslan.archeage.entity.*
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.item.UserPriceKey
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import com.arslan.archeage.toDTO
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PackTest {

    private val archeageServer = ArcheageServer("ARCHEAGE_SERVER",1)
    private val createLocation = Location("CREATE_LOCATION",Continent.WEST,archeageServer,false,1)
    private val sellLocation = Location("SELL_LOCATION",Continent.WEST,archeageServer,true,2)
    private val category = Category("SOME_CATEGORY",null,archeageServer,1)
    private val item = Item("SOME_ITEM","SOME_DESC",archeageServer,1)
    private val purchasableItem = PurchasableItem("PURCHASABLE_ITEM","SOME_DESC",archeageServer).apply { id = 2 }
    private val user = User("SOME_EMAIL","PASSWORD",UserRole.USER,1)
    private val userPrice = mapOf(purchasableItem.id!! to UserPrice(UserPriceKey(user,purchasableItem),Price(2,0,0)))
    private val percentage = 120

    @Test
    fun `should convert pack to dto`() {
        val pack = Pack(createLocation,PackPrice(Price(100,0,0),sellLocation),10,category,100,"PACK","DESCRIPTION").apply {
            id = 1
            addMaterial(CraftingMaterial(10,item))
            addMaterial(CraftingMaterial(5,purchasableItem))
        }
        val expectedDTO = with(pack){ PackDTO(name,createLocation.name,sellLocation.name,price.price,producedQuantity,materials().map { it.toDTO(userPrice) },id!!,Price(10,0,0),percentage,Price(110,0,0)) }

        pack.toDTO(userPrice,percentage) shouldBe expectedDTO
    }

    @Test
    fun `should convert packs to dto`() {
        val pack = Pack(createLocation,PackPrice(Price(100,0,0),sellLocation),10,category,100,"PACK","DESCRIPTION").apply {
            id = 1
            addMaterial(CraftingMaterial(10,item))
            addMaterial(CraftingMaterial(5,purchasableItem))
        }
        val expectedDTO = with(pack){ PackDTO(name,createLocation.name,sellLocation.name,price.price,producedQuantity,materials().map { it.toDTO(userPrice) },id!!,Price(10,0,0),percentage,Price(110,0,0)) }

        listOf(pack).toDTO(userPrice,mapOf(pack.id!! to percentage)).shouldContainExactly(expectedDTO)
    }
}