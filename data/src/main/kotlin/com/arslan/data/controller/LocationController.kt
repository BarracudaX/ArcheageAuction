package com.arslan.data.controller

import com.arslan.data.entity.Continent
import com.arslan.data.entity.Location
import com.arslan.data.service.LocationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/locations")
@RequestMapping("/locations")
class LocationController(private val locationService: LocationService) {

    @GetMapping("/{continent}")
    fun continentLocations(@PathVariable continent: Continent) : ResponseEntity<List<Location>> = ResponseEntity.ok(locationService.continentLocations(continent))

    @GetMapping("/factory/{continent}")
    fun continentFactories(@PathVariable continent: Continent) : ResponseEntity<List<Location>> = ResponseEntity.ok(locationService.continentFactories(continent))

}