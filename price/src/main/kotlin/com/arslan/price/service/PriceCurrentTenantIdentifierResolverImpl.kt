package com.arslan.price.service

import org.hibernate.cfg.AvailableSettings
import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.stereotype.Component

@Component
class PriceCurrentTenantIdentifierResolverImpl(@Value("\${base.schema.name}") private val baseName: String) : CurrentTenantIdentifierResolver,HibernatePropertiesCustomizer {
    override fun resolveCurrentTenantIdentifier(): String = "${baseName}_${ArcheageServerContextHolder.getServerContext()}"

    override fun validateExistingCurrentSessions(): Boolean = false

    override fun customize(hibernateProperties: MutableMap<String, Any>) {
        hibernateProperties[AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER] = this
    }


}