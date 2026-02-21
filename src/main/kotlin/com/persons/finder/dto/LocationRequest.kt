package com.persons.finder.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min

open class LocationRequest(
    @field:Min(value = -90, message = "Latitude must be between -90 and 90")
    @field:Max(value = 90, message = "Latitude must be between -90 and 90")
    open val latitude: Double,

    @field:Min(value = -180, message = "Longitude must be between -180 and 180")
    @field:Max(value = 180, message = "Longitude must be between -180 and 180")
    open val longitude: Double )
{}