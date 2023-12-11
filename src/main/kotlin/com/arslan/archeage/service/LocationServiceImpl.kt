package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Location
import com.arslan.archeage.repository.LocationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LocationServiceImpl(private val locationRepository: LocationRepository) : LocationService {

    @Transactional(readOnly = true)
    override fun continentLocations(continent: Continent,archeageServer: ArcheageServer): List<Location> {
        return locationRepository.findByContinentAndArcheageServer(continent,archeageServer)
    }
    override fun continentFactories(continent: Continent,archeageServer: ArcheageServer): List<Location>{
        return locationRepository.findByContinentAndHasFactoryIsTrueAndArcheageServer(continent,archeageServer)
    }

}