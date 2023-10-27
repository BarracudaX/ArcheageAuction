package com.arslan.archeage.service

import com.arslan.archeage.Continent
import com.arslan.archeage.entity.Location
import com.arslan.archeage.repository.LocationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LocationServiceImpl(private val locationRepository: LocationRepository) : LocationService {

    @Transactional(readOnly = true)
    override fun continentLocations(continent: Continent): List<Location> {
        if(ArcheageServerContextHolder.getServerContext() == null) return emptyList()

        return locationRepository.findByContinentAndRegion(continent,ArcheageServerContextHolder.getServerContext()!!.region)
    }
    override fun continentFactories(continent: Continent): List<Location>{
        if(ArcheageServerContextHolder.getServerContext() == null) return emptyList()
        
        return locationRepository.findByContinentAndHasFactoryIsTrueAndRegion(continent,ArcheageServerContextHolder.getServerContext()!!.region)
    }

}