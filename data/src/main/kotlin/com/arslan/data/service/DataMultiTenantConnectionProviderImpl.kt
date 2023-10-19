package com.arslan.data.service

import org.hibernate.cfg.AvailableSettings
import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider
import org.hibernate.service.UnknownUnwrapTypeException
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.stereotype.Component
import java.sql.Connection
import javax.sql.DataSource

@Component
class DataMultiTenantConnectionProviderImpl(private val datasource: DataSource, private val tenantIdentifierResolver: CurrentTenantIdentifierResolver) : MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

    override fun isUnwrappableAs(unwrapType: Class<*>?): Boolean = false

    override fun <T : Any?> unwrap(unwrapType: Class<T>?): T = throw UnknownUnwrapTypeException(unwrapType)

    override fun getAnyConnection(): Connection = datasource.connection.apply {
        createStatement().execute("USE Archeage_en")
    }

    override fun releaseAnyConnection(connection: Connection) {
        connection.close()
    }

    override fun getConnection(tenantIdentifier: String): Connection = datasource.connection.apply {
        createStatement().execute("USE $tenantIdentifier")
    }

    override fun releaseConnection(tenantIdentifier: String, connection: Connection) {
        connection.close()
    }

    override fun supportsAggressiveRelease(): Boolean = false

    override fun customize(hibernateProperties: MutableMap<String, Any>) {
        hibernateProperties[AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER] = this
    }

}