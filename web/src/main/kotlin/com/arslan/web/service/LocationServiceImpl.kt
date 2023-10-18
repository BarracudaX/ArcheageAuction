package com.arslan.web.service

import com.arslan.web.LocationDTO
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.util.stream.Collectors

@Service
class LocationServiceImpl(@Qualifier("dataWebClient") private val webClient: WebClient) : LocationService {

    override fun locations(): List<LocationDTO> = webClient.get()
        .uri("/locations")
        .header(HttpHeaders.ACCEPT_LANGUAGE,LocaleContextHolder.getLocale().language)
        .retrieve()
        .bodyToFlux(LocationDTO::class.java)
        .collectList().block()!!

}