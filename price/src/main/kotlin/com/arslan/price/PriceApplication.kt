package com.arslan.price

import com.arslan.price.service.PriceArcheageServerContextHolder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PriceApplication

fun main(args: Array<String>) {
    PriceArcheageServerContextHolder.setServerContext("nagashar")
    runApplication<PriceApplication>(*args)
}