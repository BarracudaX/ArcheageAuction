package com.arslan.archeage

import com.arslan.archeage.entity.Price
import com.arslan.archeage.event.ItemPriceChangeEvent
import com.arslan.archeage.repository.UserPriceRepository
import com.arslan.archeage.service.PackProfitService
import com.arslan.archeage.service.PackProfitServiceImpl
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("dev")
@Component
class PriceInitializer(private val userPriceRepository: UserPriceRepository,private val profitService: PackProfitService) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        userPriceRepository.findAll().forEach { price ->
            profitService.onItemPriceChange(ItemPriceChangeEvent(this,price.id.purchasableItem,price.id.user,Price.ZERO))
        }
    }

}