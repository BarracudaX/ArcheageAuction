package com.arslan.web

import kotlinx.serialization.Serializable

enum class Continent{ WEST,EAST,NORTH }

@Serializable
data class LocationDTO(val continent: Continent,val name: String)