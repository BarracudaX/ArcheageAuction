package com.arslan.archeage.service

import com.arslan.archeage.repository.UserRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class ArcheageAuctionWebAuthenticationProvider(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val user = userRepository.findByEmail(authentication.principal.toString()) ?: throw UsernameNotFoundException("User ${authentication.principal} not found.")

        if(!passwordEncoder.matches(authentication.credentials.toString(),user.password)) throw BadCredentialsException("Invalid credentials provided.")

        return UsernamePasswordAuthenticationToken(user.email,user.password, emptyList())
    }

    override fun supports(authentication: Class<*>): Boolean = UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)


}