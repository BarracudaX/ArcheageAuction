package com.arslan.web.service

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.ConcurrentHashMap


@Service
class ServerServiceImpl(private val webClient: WebClient) : ServerService {

    private val servers = ConcurrentHashMap<String,List<String>>()

    override fun servers(): List<String> {
        return servers.computeIfAbsent(LocaleContextHolder.getLocale().language){
            webClient.get().uri("/servers")
                .headers { it.acceptLanguageAsLocales = listOf(LocaleContextHolder.getLocale()) }
                .retrieve()
                .bodyToMono(object : ParameterizedTypeReference<List<String>>() {})
                .block()!!
        }
    }

}