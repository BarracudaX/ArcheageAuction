package com.arslan.archeage.config

import jakarta.servlet.*
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter

@Configuration
class SecurityConfiguration {

    @FunctionalInterface
    interface HttpSecurityConfigurer{

        fun configure(httpSecurity: HttpSecurity) : HttpSecurity

    }

    @Bean
    fun roleHierarchy() : RoleHierarchy = RoleHierarchyImpl().apply {
        setHierarchy("ROLE_ADMIN > ROLE_USER")
    }

    @Order(1)
    @Bean
    fun defaultHttpSecurityConfigurer(authenticationProvider: AuthenticationProvider) : HttpSecurityConfigurer = object :
        HttpSecurityConfigurer {
        override fun configure(httpSecurity: HttpSecurity): HttpSecurity = httpSecurity
            .formLogin{form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
            }.logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/login")
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers(HttpMethod.GET,"/locations","/","/packs","/favicon.ico","/login","/logout","/error").permitAll()
                    .requestMatchers(HttpMethod.GET,"/resource/**").permitAll()
            }
            .anonymous {  }
            .authenticationProvider(authenticationProvider)
    }

    @Order(2)
    @Profile("dev")
    @Bean
    fun devHttpSecurityConfigurer() : HttpSecurityConfigurer = object : HttpSecurityConfigurer {
        override fun configure(httpSecurity: HttpSecurity): HttpSecurity {
            return httpSecurity
                .authorizeHttpRequests{ authorize ->
                    authorize.requestMatchers(EndpointRequest.to("prometheus")).permitAll()
                }.csrf { csrf -> csrf.disable() }
        }
    }

    /**
     * If put in the default configurer, then dev configurer throws an exception because after calling anyRequest, no additional rules can be specified, and instead spring throws IAE.
     */
    @Order(3)
    @Bean
    fun httpSecurityConfigurerDenyAll() : HttpSecurityConfigurer = object : HttpSecurityConfigurer {
        override fun configure(httpSecurity: HttpSecurity): HttpSecurity = httpSecurity.authorizeHttpRequests { authorize -> authorize.anyRequest().denyAll() }

    }

    @Bean
    fun securityFilterChainBuilder(http: HttpSecurity, configurers: List<HttpSecurityConfigurer>) : SecurityFilterChain{
        var httpSecurity = http
        for (configurer in configurers){
            httpSecurity = configurer.configure(httpSecurity)
        }
        return httpSecurity.build()
    }

}