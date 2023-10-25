package com.arslan.archeage.repository

import com.arslan.archeage.entity.Pack
import org.springframework.data.jpa.repository.JpaRepository

interface PackRepository : JpaRepository<Pack,Long>