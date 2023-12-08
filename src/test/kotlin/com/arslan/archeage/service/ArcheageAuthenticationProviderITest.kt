package com.arslan.archeage.service

import com.arslan.archeage.entity.User
import com.arslan.archeage.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

class ArcheageAuthenticationProviderITest(
    private val authenticationProvider: ArcheageAuthenticationProvider,
    private val passwordEncoder: PasswordEncoder
) : AbstractITest() {

    @Test
    fun `should throw UsernameNotFoundException when provided non existing principal`() {
        shouldThrow<UsernameNotFoundException> {
            authenticationProvider.authenticate(UsernamePasswordAuthenticationToken("non-existing-principal","any"))
        }
    }

    @Test
    fun `should throw BadCredentialsException if provided invalid credentials`() {
        val (principal,password) = "any_principal" to "any_password"
        userRepository.save(User(principal,passwordEncoder.encode(password)))

        shouldThrow<BadCredentialsException> { authenticationProvider.authenticate(UsernamePasswordAuthenticationToken(principal,password.plus("1"))) }
    }

    @Test
    fun `should return authenticated Authentication with user's role as granted authority and empty credentials on successful authentication`() {
        val (username,password) = "any_principal" to "any_password"
        val user = userRepository.save(User(username,passwordEncoder.encode(password)))

        val result = authenticationProvider.authenticate(UsernamePasswordAuthenticationToken(username,password))

        result.isAuthenticated.shouldBeTrue()
        result.principal shouldBe user.id!!
        result.credentials shouldBe ""
        result.authorities shouldHaveSize 1
        result.authorities.shouldContainExactly(SimpleGrantedAuthority(user.role.name))
    }


}