package com.arslan.data.service

import com.arslan.data.PackDTO
import com.arslan.data.RecipeDTO
import com.arslan.data.entity.Continent
import com.arslan.data.entity.Pack
import com.arslan.data.entity.Recipe
import com.arslan.data.repository.LocationRepository
import com.arslan.data.repository.PackRepository
import com.arslan.data.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PackServiceImpl(private val locationRepository: LocationRepository,private val packRepository: PackRepository) : PackService {

    @Transactional(readOnly = true)
    override fun packsAtLocation(location: String): List<Pack> {
        return packRepository.findByCreationLocation(locationRepository.findByName(location))
    }

    override fun packAtContinent(continent: Continent): List<PackDTO> = packRepository
        .findByContinent(continent)
        .map(Pack::toDTO)

}