package com.arslan.web.service

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service

@Service
class ServerTranslator {


    private val fromLocaleToEnglish = mapOf(
        "луций" to "lucius",
        "корвус" to "corvus",
        "фанем" to "fanem",
        "шаеда" to "shaeda",
        "ифнир" to "ifnir",
        "ксанатос" to "xanatos",
        "тарон" to "taron",
        "рейвен" to "raven",
        "нагашар" to "nagashar"
    )

    private val fromEnglishToLocale = mapOf(
        "lucius" to "луций" ,
        "corvus" to "корвус",
        "fanem" to "фанем" ,
        "shaeda" to "шаеда",
        "ifnir" to "ифнир",
        "xanatos" to "ксанатос",
        "taron" to "тарон",
        "raven" to "рейвен",
        "nagashar" to "нагашар"
    )

    fun translateToUseWithPriceService(server: String) : String = fromLocaleToEnglish.getOrDefault(server,server)

    fun translateToUseWithView(server: String) : String = if(LocaleContextHolder.getLocale().language != "en"){
        fromEnglishToLocale[server]!!
    }else{
        server
    }
}