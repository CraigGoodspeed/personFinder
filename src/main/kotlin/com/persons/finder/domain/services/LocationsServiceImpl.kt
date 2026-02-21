package com.persons.finder.domain.services

import com.persons.finder.data.Location
import com.persons.finder.repository.LocationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocationsServiceImpl @Autowired constructor(private val locationRepository: LocationRepository) : LocationsService {

    override fun addLocation(location: Location) {
        locationRepository.save(location)
    }

    override fun removeLocation(locationReferenceId: Long) {
        //TODO: we could potentially store this data before removing it?
        locationRepository.deleteById(locationReferenceId)
    }

    override fun findAround(latitude: Double, longitude: Double, radiusInKm: Double): List<Location> {
        return locationRepository.findLocationsWithinRadius(
            latitude,
            longitude,
            radiusInKm
        )
    }

}