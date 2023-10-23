package com.arslan.data.service

import com.arslan.data.entity.Continent
import com.arslan.data.entity.Location
import com.arslan.data.repository.LocationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LocationServiceImpl(private val locationRepository: LocationRepository) : LocationService {

    @Transactional(readOnly = true)
    override fun continentLocations(continent: Continent): List<Location> = locationRepository.findByContinent(continent)
    override fun continentFactories(continent: Continent): List<Location> = locationRepository.findByContinentAndHasFactoryIsTrue(continent)

}