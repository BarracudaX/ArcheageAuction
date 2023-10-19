package com.arslan.data.service

import com.arslan.data.entity.Pack
import com.arslan.data.repository.LocationRepository
import com.arslan.data.repository.PackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PackServiceImpl(private val locationRepository: LocationRepository,private val packRepository: PackRepository) : PackService {

    @Transactional(readOnly = true)
    override fun findPacksAtLocation(location: String): List<Pack> {
        return packRepository.findByCreationLocation(locationRepository.findByName(location))
    }

}