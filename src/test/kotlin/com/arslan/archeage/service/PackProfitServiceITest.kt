package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.Location
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.pack.Pack
import com.arslan.archeage.entity.pack.PackPrice
import org.junit.jupiter.api.BeforeEach
import kotlin.random.Random

class PackProfitServiceITest(private val packProfitService: PackProfitService) : AbstractITest() {

    private val allMaterials = mutableListOf<Item>()
    private val purchasableMaterials = mutableListOf<PurchasableItem>()
    private lateinit var pack: Pack

    @BeforeEach
    fun setUp(){
        val archeageServer = archeageServerRepository.save(ArcheageServer("ANY"))
        allMaterials.add(itemRepository.save(Item("ITEM_1","ANY",archeageServer)))
        allMaterials.add(itemRepository.save(Item("ITEM_2","ANY",archeageServer)))
        allMaterials.add(itemRepository.save(PurchasableItem("PURCHASABLE_1","ANY",archeageServer)))
        allMaterials.add(itemRepository.save(PurchasableItem("PURCHASABLE_2","ANY",archeageServer)))

        purchasableMaterials.addAll(allMaterials.filterIsInstance<PurchasableItem>())

        val location = locationRepository.save(Location("ANY_LOCATION",Continent.WEST,archeageServer,true))
        pack = packRepository.save(Pack(location,PackPrice(Price(30,20,10),location),5,"PACK","ANY"))
        allMaterials.forEach { material -> pack.addMaterial(CraftingMaterial(Random.nextInt(10),material)) }
    }


}