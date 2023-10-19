package com.arslan.data.repository

import com.arslan.data.entity.Server
import org.springframework.data.jpa.repository.JpaRepository

interface ServerRepository : JpaRepository<Server,String>