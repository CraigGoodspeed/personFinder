package com.persons.finder.dto

import com.persons.finder.SpatialConstants.HALF_OF_EARTH_CIRCUMFERENCE
import javax.validation.constraints.Max
import javax.validation.constraints.Positive

data class NearbySearchRequest(
    override val latitude: Double,
    override val longitude: Double,
    @field:Positive(message = "radius must be greater than zero")
    @field:Max(value = HALF_OF_EARTH_CIRCUMFERENCE, message = "Radius cannot exceed 20,000km (half the Earth's circumference)")
    val radius : Double,
) : LocationRequest(latitude, longitude) {
}