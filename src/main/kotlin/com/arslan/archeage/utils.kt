package com.arslan.archeage

import com.arslan.archeage.entity.CraftingMaterial
import com.arslan.archeage.entity.Price
import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.UserPrice
import com.arslan.archeage.entity.pack.Pack
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.util.MultiValueMap

fun CraftingMaterial.toDTO(prices: Map<Long, UserPrice>): CraftingMaterialDTO = CraftingMaterialDTO(quantity, item.toDTO(prices[item.id!!]?.price),prices[item.id!!]?.price?.times(quantity))

fun Item.toDTO(price: Price?): ItemDTO = ItemDTO(name, id!!,price)

fun Pack.toDTO(prices: Map<Long, UserPrice>,percentage: Int,isUserData: Boolean) : PackDTO {
    val cost = materials().fold(Price(0,0,0)){ cost,material -> cost + (prices[material.item.id!!]?.price ?: Price(0,0,0))*material.quantity }

    val profit = price.price*(percentage/100.0) - cost
    val workingPointProfit = profit.div(workingPoints)

    return PackDTO(name,creationLocation.name,price.sellLocation.name,price.price,producedQuantity,materials().map { it.toDTO(prices) } ,id!!,cost,percentage,profit,workingPointProfit,isUserData)
}

fun List<Pack>.toDTO(prices: Map<Long, UserPrice>,percentages: Map<Long,Int>,isUserData: Boolean): List<PackDTO> = map { pack -> pack.toDTO(prices,percentages[pack.id!!]!!,isUserData) }

fun Price.initialValue(percentage: Double) : Price = this * (1/percentage)

fun MultiValueMap<String, String>.toPacksRequest(userID: Long?): PackRequest{
    val continent = Continent.valueOf(this["continent"]!![0])
    val departureLocation = this["departureLocation"]?.get(0)?.toLongOrNull()
    val destinationLocation = this["destinationLocation"]?.get(0)?.toLongOrNull()
    val categories = this["categories[]"]?.map { it.removePrefix("categories=") }?.map(String::toLong) ?: emptyList()
    return PackRequest(continent,departureLocation,destinationLocation,userID,categories)
}

fun MultiValueMap<String,String>.pageable() : Pageable{
    val orders = this
        .filter { it.key.startsWith("order") && (it.key.contains("name") || it.key.contains("dir")) }
        .map { it.key.split("[").map { part -> part.removeSuffix("]") } to it.value[0] }
        .groupBy({ (param, _) -> param[1] }){ (_,value) ->
            value
        }.toSortedMap()
        .values
        .flatMap { order ->
            val (direction,property) = order[0] to order[1]
            when(property){
                "profit" ->{
                    if(direction == "desc"){
                        listOf(Sort.Order.desc("netProfit.gold"),Sort.Order.desc("netProfit.silver"),Sort.Order.desc("netProfit.copper"))
                    }else{
                        listOf(Sort.Order.asc("netProfit.gold"),Sort.Order.asc("netProfit.silver"),Sort.Order.asc("netProfit.copper"))
                    }
                }
                "profitPerWorkingPoint" -> {
                    if(direction == "desc"){
                        listOf(Sort.Order.desc("workingPointsProfit.gold"),Sort.Order.desc("workingPointsProfit.silver"),Sort.Order.desc("workingPointsProfit.copper"))
                    }else{
                        listOf(Sort.Order.asc("workingPointsProfit.gold"),Sort.Order.asc("workingPointsProfit.silver"),Sort.Order.asc("workingPointsProfit.copper"))
                    }
                }
                else -> throw IllegalArgumentException("Unknown sorting property $order.")
            }
        }

    val pageSize = get("length")!![0].toInt()
    val pageNumber = get("start")!![0].toInt()/pageSize
    return PageRequest.of(pageNumber,pageSize,Sort.by(orders))
}

fun Page<PackDTO>.toDataTableResponse(params: MultiValueMap<String,String>,numOfPacks: Long,userID: Long?) : DataTableResponse = DataTableResponse(params["draw"]!![0].toInt(),numOfPacks,totalElements,content)