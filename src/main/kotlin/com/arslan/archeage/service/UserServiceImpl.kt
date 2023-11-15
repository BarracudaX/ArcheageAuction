package com.arslan.archeage.service

import com.arslan.archeage.controllers.RegistrationForm
import com.arslan.archeage.entity.User
import com.arslan.archeage.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserServiceImpl(private val userRepository: UserRepository,private val passwordEncoder: PasswordEncoder) : UserService {

    override fun register(registrationForm: RegistrationForm) {
        with(registrationForm){ userRepository.save(User(email,passwordEncoder.encode(password))) }
    }

}