package com.arslan.archeage.config

import com.arslan.archeage.web.ArcheageServerChangeInterceptor
import com.arslan.archeage.web.ArcheageServerContextHandlerInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.LocaleContextResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
class WebConfiguration(private val archeageServerContextHandlerInterceptor: ArcheageServerContextHandlerInterceptor, private val archeageServerChangeInterceptor: ArcheageServerChangeInterceptor) : WebMvcConfigurer {

    @Bean
    fun localeResolver() : LocaleContextResolver = CookieLocaleResolver("language")

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LocaleChangeInterceptor()).order(1)
        registry.addInterceptor(archeageServerChangeInterceptor).order(2)
        registry.addInterceptor(archeageServerContextHandlerInterceptor).order(3)
    }
}