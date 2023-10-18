package com.arslan.data.controller

import com.arslan.data.entity.Location
import com.arslan.data.service.LocationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/locations")
@RequestMapping("/locations")
class LocationController(private val locationService: LocationService) {

    @GetMapping
    fun locations() : ResponseEntity<List<Location>> = ResponseEntity.ok(locationService.locations())

}