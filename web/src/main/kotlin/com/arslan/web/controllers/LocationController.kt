package com.arslan.web.controllers

import com.arslan.web.LocationDTO
import com.arslan.web.service.LocationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/locations")
class LocationController(private val locationService: LocationService) {

    @GetMapping
    fun locations() : ResponseEntity<List<LocationDTO>> = ResponseEntity.ok(locationService.locations())
}