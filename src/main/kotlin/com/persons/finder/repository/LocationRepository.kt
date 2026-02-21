package com.persons.finder.repository

import com.persons.finder.SpatialConstants
import com.persons.finder.data.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<Location, Long> {

    @Query("""
    SELECT l FROM Location l 
    WHERE (:earthRadius * acos(
        cos(radians(:lat)) * cos(radians(l.latitude)) * cos(radians(l.longitude) - radians(:lon)) + 
        sin(radians(:lat)) * sin(radians(l.latitude))
    )) <= :radius
""")
    fun findLocationsWithinRadius(
        @Param("lat") lat: Double,
        @Param("lon") lon: Double,
        @Param("radius") radius: Double,
        @Param("earthRadius") earthRadius: Double = SpatialConstants.EARTH_RADIUS_KM
    ): List<Location>
}