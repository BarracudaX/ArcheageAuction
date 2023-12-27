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
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RequestMapping("/locations")
@RestController
class LocationController(private val locationService: LocationService) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun locations(@ModelAttribute request: LocationRequest, archeageServer: ArcheageServer?) : ResponseEntity<Locations>{
        if(archeageServer == null) throw ArcheageContextHolderEmptyException()
        val continentLocations = locationService.continentLocations(request.continent,archeageServer)
            .filter { request.destinationLocation.isEmpty || request.destinationLocation.get() != it.id }
            .map { LocationDTO(it.name,it.id!!) }
        val continentFactories = locationService.continentFactories(request.continent,archeageServer)
            .filter { request.departureLocation.isEmpty || request.departureLocation.get() != it.id }
            .map { LocationDTO(it.name,it.id!!) }

        return ResponseEntity.ok(Locations(continentLocations,continentFactories))
    }

    data class LocationRequest(val continent: Continent, val destinationLocation: Optional<Long>, val departureLocation: Optional<Long>)

}