package com.arslan.archeage.repository

import com.arslan.archeage.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User,Long>{

    fun findByEmail(email: String) : User?

}