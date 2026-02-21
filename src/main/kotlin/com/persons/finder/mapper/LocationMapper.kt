package com.persons.finder.mapper

import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.dto.LocationRequest
import org.springframework.stereotype.Component

@Component
class LocationMapper {
    fun toLocation(person: Person, location: LocationRequest) : Location {
        return Location(
            latitude = location.latitude,
            longitude = location.longitude,
            person = person
        )
    }
}