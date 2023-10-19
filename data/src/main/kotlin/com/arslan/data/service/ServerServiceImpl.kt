package com.arslan.data.service

import com.arslan.data.entity.Server
import com.arslan.data.repository.ServerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ServerServiceImpl(private val serverRepository: ServerRepository) : ServerService {

    @Transactional(readOnly = true)
    override fun servers(): List<Server> = serverRepository.findAll()

}