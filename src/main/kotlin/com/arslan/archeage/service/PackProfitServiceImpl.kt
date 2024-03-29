package com.arslan.archeage.service

import com.arslan.archeage.PackPercentageUpdate
import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.item.PurchasableItem
import com.arslan.archeage.entity.pack.PackProfit
import com.arslan.archeage.entity.pack.PackProfitKey
import com.arslan.archeage.event.ItemPriceChangeEvent
import com.arslan.archeage.initialValue
import com.arslan.archeage.repository.PackProfitRepository
import com.arslan.archeage.repository.PackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionalEventListener

@Transactional
@Service
class PackProfitServiceImpl(private val packRepository: PackRepository,private val packProfitRepository: PackProfitRepository,private val itemPriceService: ItemPriceService) : PackProfitService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    override fun onItemPriceChange(event: ItemPriceChangeEvent) {
        val packs = packRepository.selectPacksWithCraftingMaterial(event.item, event.user)
        val changedPacks = packProfitRepository
            .findAllByIdIn(packs.map { PackProfitKey(it,event.user) })
            .map { profit ->
                profit.netProfit += event.priceChange*profit.id.pack.requiredQuantity(event.item)
                profit.workingPointsProfit = profit.netProfit/profit.id.pack.workingPoints
                profit.id.pack.id!!
            }
        val requireNewProfit = packs.filter { !changedPacks.contains(it.id!!) }

        if(requireNewProfit.isEmpty()) return

        val prices = itemPriceService.userItemPrices(requireNewProfit.flatMap { pack -> pack.materials().map(CraftingMaterial::item).filterIsInstance<PurchasableItem>() },event.user.id!!)

        requireNewProfit.forEach { pack ->
            val netProfit = pack.profit(prices.mapValues { it.value.price },1.0)
            val workingPointsProfit = netProfit / pack.workingPoints
            packProfitRepository.save(PackProfit(PackProfitKey(pack,event.user),netProfit,workingPointsProfit))
        }
    }

    override fun updatePercentage(update: PackPercentageUpdate) {
        packProfitRepository
            .findPackProfit(update.packID,update.userID!!)
            .apply {
                netProfit = netProfit.initialValue(percentage/100.0)*(update.percentage/100.0)
                workingPointsProfit = netProfit/id.pack.workingPoints
                percentage = update.percentage
            }
    }

}