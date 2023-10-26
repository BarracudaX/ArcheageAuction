package com.arslan.archeage.service

import com.arslan.archeage.entity.User
import com.arslan.archeage.repository.UserRepository
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
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository
) : AbstractITest() {

    @Test
    fun `should throw UsernameNotFoundException when provided non existing principal`() {
        assertThrows<UsernameNotFoundException> {
            authenticationProvider.authenticate(UsernamePasswordAuthenticationToken("non-existing-principal","any"))
        }
    }

    @Test
    fun `should throw BadCredentialsException if provided invalid credentials`() {
        val (principal,password) = "any_principal" to "any_password"
        userRepository.save(User(principal,passwordEncoder.encode(password)))

        assertThrows<BadCredentialsException> { authenticationProvider.authenticate(UsernamePasswordAuthenticationToken(principal,password.plus("1"))) }
    }

    @Test
    fun `should return authenticated Authentication with user's role as granted authority and empty credentials on successful authentication`() {
        val (principal,password) = "any_principal" to "any_password"
        val user = userRepository.save(User(principal,passwordEncoder.encode(password)))

        val result = authenticationProvider.authenticate(UsernamePasswordAuthenticationToken(principal,password))

        assertTrue(result.isAuthenticated)
        assertEquals(principal,result.principal)
        assertEquals("",result.credentials)
        assertEquals(1,result.authorities.size)
        assertEquals(SimpleGrantedAuthority(user.role.name),result.authorities.iterator().next())
    }


}