package com.arslan.data.service

import org.hibernate.cfg.AvailableSettings
import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class DataCurrentTenantIdentifierResolverImpl(@Value("\${base.schema.name}") private val baseName: String) : CurrentTenantIdentifierResolver,HibernatePropertiesCustomizer {

    private val supportedLocales = mapOf<String,String>(Locale.ENGLISH.language to Locale.ENGLISH.language,Locale("ru","RU").language to Locale("ru","RU").language )

    override fun resolveCurrentTenantIdentifier(): String = "${baseName}_${supportedLocales.getOrDefault(LocaleContextHolder.getLocale().language,Locale.ENGLISH.language)}"
    override fun validateExistingCurrentSessions(): Boolean = true
    override fun customize(hibernateProperties: MutableMap<String, Any>) {
        hibernateProperties[AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER] = this
    }

}