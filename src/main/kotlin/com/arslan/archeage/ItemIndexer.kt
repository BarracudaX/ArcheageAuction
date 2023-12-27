package com.arslan.archeage

import com.arslan.archeage.entity.item.Item
import jakarta.persistence.EntityManagerFactory
import org.hibernate.Session
import org.hibernate.search.mapper.orm.Search
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("dev")
@Component
class ItemIndexer(private val entityManagerFactory: EntityManagerFactory) : ApplicationRunner{

    override fun run(args: ApplicationArguments?) {
        val entityManager = entityManagerFactory.createEntityManager()
        val searchSession = Search.session(entityManager.unwrap(Session::class.java))

        val itemIndexer = searchSession.massIndexer(Item::class.java).threadsToLoadObjects(Runtime.getRuntime().availableProcessors())
        itemIndexer.startAndWait()
    }

}