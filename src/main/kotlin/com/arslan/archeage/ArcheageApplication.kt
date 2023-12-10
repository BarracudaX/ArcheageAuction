package com.arslan.archeage

import com.arslan.archeage.entity.item.Item
import com.arslan.archeage.entity.item.PurchasableItem
import jakarta.persistence.EntityManagerFactory
import org.hibernate.Session
import org.hibernate.search.mapper.orm.Search
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ElasticsearchRestClientAutoConfiguration::class])
class ArcheageApplication

fun main(args: Array<String>) {
	val context = runApplication<ArcheageApplication>(*args)
	val entityManagerFactory = context.getBean(EntityManagerFactory::class.java)
	val entityManager = entityManagerFactory.createEntityManager()
	val searchSession = Search.session(entityManager.unwrap(Session::class.java))

	val itemIndexer = searchSession.massIndexer(Item::class.java).threadsToLoadObjects(Runtime.getRuntime().availableProcessors())
	itemIndexer.startAndWait()
	println("Done indexing")
}
