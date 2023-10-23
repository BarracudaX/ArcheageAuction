package com.arslan.web.service

import com.arslan.web.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux

@Service
class PackServiceImpl(
    @Qualifier("dataWebClient") private val dataService: WebClient,
    @Qualifier("defaultWebClient") private val priceService: WebClient,
    @Value("\${price.app.base.url}") private val priceAppBaseUrl: String
) : PackService {

    override fun packs(continent: Continent): List<PackDTO>{
        val dataPacks = dataService
            .get()
            .uri("/packs/continent/${continent}")
            .headers { it.acceptLanguageAsLocales = listOf(LocaleContextHolder.getLocale()) }
            .retrieve()
            .bodyToFlux<DataPackDTO>()
            .collectList()
            .block()!!

        val items = dataPacks.flatMap { pack ->
           pack.recipes.flatMap { recipe -> recipe.materials.map{ material -> material.material.name}}.plus(pack.name)
        }.toSet()

        val prices = priceService
            .post()
            .uri{ builder ->
                builder
                    .host(priceAppBaseUrl.replace("{server}",WebArcheageServerContextHolder.getServerContext()!!))
                    .scheme("http")
                    .port(8070)
                    .path("/item")
                    .build()
            }
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(items)
            .retrieve()
            .bodyToFlux<ItemPriceDTO>()
            .collectList()
            .block()!!
            .groupBy { it.itemName }


        return dataPacks.flatMap {dataPack -> dataPack.toPackDTO(prices)}
    }

}