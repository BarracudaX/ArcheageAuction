package com.arslan.data

import com.arslan.data.entity.Continent
import com.arslan.data.entity.Location
import com.arslan.data.repository.LocationRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*


@SpringBootApplication
class DataApplication

fun main(args: Array<String>) {
    runApplication<DataApplication>(*args)
}
