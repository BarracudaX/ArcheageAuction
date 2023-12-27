package com.arslan.archeage

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import jakarta.persistence.EntityManagerFactory
import org.hibernate.Session
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration
import org.springframework.boot.runApplication
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader

@SpringBootApplication(exclude = [ElasticsearchRestClientAutoConfiguration::class])
class ArcheageApplication

fun main(args: Array<String>) {
	runApplication<ArcheageApplication>(*args)
}
