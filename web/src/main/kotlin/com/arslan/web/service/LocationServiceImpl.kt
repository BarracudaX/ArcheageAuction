package com.arslan.web.service

import com.arslan.web.Continent
import com.arslan.web.LocationDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.stream.Collectors

@Service
class LocationServiceImpl(@Qualifier("dataWebClient") private val webClient: WebClient) : LocationService {

    override fun locations(continent: Continent): List<LocationDTO> = webClient.get()
        .uri("/locations/${continent.name}")
        .header(HttpHeaders.ACCEPT_LANGUAGE,LocaleContextHolder.getLocale().language)
        .retrieve()
        .bodyToFlux(LocationDTO::class.java)
        .collectList().block()!!

    override fun factories(continent: Continent): List<LocationDTO> = webClient.get()
        .uri("/locations/factory/${continent.name}")
        .header(HttpHeaders.ACCEPT_LANGUAGE,LocaleContextHolder.getLocale().language)
        .retrieve()
        .bodyToFlux(LocationDTO::class.java)
        .collectList().block()!!

}