package com.persons.finder.facade

import com.persons.finder.data.Hobby
import com.persons.finder.data.Location
import com.persons.finder.domain.services.HobbyService
import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.dto.LocationRequest
import com.persons.finder.dto.NearbySearchRequest
import com.persons.finder.dto.PersonRequest
import com.persons.finder.events.model.PersonCreatedEvent
import com.persons.finder.mapper.LocationMapper
import com.persons.finder.mapper.PersonMapper
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PersonManagementService (
    private val personService: PersonsService,
    private val locationService: LocationsService,
    private val hobbyService: HobbyService,
    private val personMapper: PersonMapper,
    private val locationMapper: LocationMapper,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun registerNewPerson(request: PersonRequest) {

        val hobbyEntities = request.hobbies.map { name ->
            hobbyService.getByName(name) ?: Hobby(name = name)
        }.toMutableSet()
        val personEntity = personMapper.toPersonEntity(request, hobbyEntities)
        personService.save(personEntity)
        applicationEventPublisher.publishEvent(PersonCreatedEvent(personEntity))
        locationService.addLocation(
            personMapper.toLocationEntity(
                request,
                personEntity
            )
        )
    }

    @Transactional
    fun updatePersonLocation(personId: Long, request: LocationRequest) {
        val person = personService.getById(personId)
        locationService.removeLocation(personId)
        locationService.addLocation(
            locationMapper.toLocation(
                person,
                request
            )
        )
    }

    fun getLocationsAround(nearBySearchRequest: NearbySearchRequest): List<Location> {
        return locationService.findAround(
            nearBySearchRequest.latitude,
            nearBySearchRequest.longitude,
            nearBySearchRequest.radius
        )
    }
}