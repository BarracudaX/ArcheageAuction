package com.arslan.archeage.controllers

import com.arslan.archeage.ArcheageContextHolderEmptyException
import com.arslan.archeage.Continent
import com.arslan.archeage.LocationDTO
import com.arslan.archeage.Locations
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.service.LocationService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RequestMapping("/locations")
@RestController
class LocationController(private val locationService: LocationService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun locations(@RequestParam continent: Continent, @RequestParam destinationLocation: Optional<Long> = Optional.empty(), @RequestParam departureLocation: Optional<Long> = Optional.empty(),archeageServer: ArcheageServer?) : ResponseEntity<Locations>{
        if(archeageServer == null) throw ArcheageContextHolderEmptyException()
        val continentLocations = locationService.continentLocations(continent,archeageServer)
            .filter { destinationLocation.isEmpty || destinationLocation.get() != it.id }
            .map { LocationDTO(it.name,it.id!!) }
        val continentFactories = locationService.continentFactories(continent,archeageServer)
            .filter { departureLocation.isEmpty || departureLocation.get() != it.id }
            .map { LocationDTO(it.name,it.id!!) }

        return ResponseEntity.ok(Locations(continentLocations,continentFactories))
    }

}