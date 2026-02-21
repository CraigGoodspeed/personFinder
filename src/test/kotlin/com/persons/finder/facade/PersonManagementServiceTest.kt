package com.persons.finder.facade

import com.persons.finder.data.Hobby
import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.domain.services.LocationsService
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.dto.LocationRequest
import com.persons.finder.dto.NearbySearchRequest
import com.persons.finder.dto.PersonRequest
import com.persons.finder.mapper.LocationMapper
import com.persons.finder.mapper.PersonMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any as mockAny
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.jvm.java


@ExtendWith(MockitoExtension::class)
class PersonManagementServiceTest {
    @Mock lateinit var personService: PersonsService
    @Mock lateinit var locationService: LocationsService
    @Mock lateinit var personMapper: PersonMapper
    @Mock lateinit var locationMapper: LocationMapper
    @InjectMocks lateinit var service: PersonManagementService



    @Test
    fun `registerNewPerson should map, save person, and then add location`() {
        // Arrange
        val golfHobby = mutableSetOf(Hobby(0, "Golf"))
        val request = PersonRequest("John", "Dev", listOf("Golf"), 10.0, 20.0)
        val personEntity = Person(id = 1, name = "John", jobTitle = "Dev", hobbies = golfHobby)
        val locationEntity = Location(latitude = 10.0, longitude = 20.0, person = personEntity)

        whenever(personMapper.toPersonEntity(request, golfHobby)).thenReturn(personEntity)
        whenever(personMapper.toLocationEntity(request, personEntity)).thenReturn(locationEntity)

        // Act
        service.registerNewPerson(request)

        // Assert
        verify(personService).save(personEntity)
        verify(locationService).addLocation(locationEntity)
    }

    @Test
    fun `updatePersonLocation should fetch person, remove old location, and add new one`() {
        // Arrange
        val golfHobby = mutableSetOf(Hobby(0, "Golf"))
        val personId = 1L
        val request = LocationRequest(30.0, 40.0)
        val person = Person(id = personId, name = "John", jobTitle = "Dev", hobbies = golfHobby)
        val newLocation = Location(latitude = 30.0, longitude = 40.0, person = person)

        whenever(personService.getById(personId)).thenReturn(person)
        whenever(locationMapper.toLocation(person, request)).thenReturn(newLocation)

        // Act
        service.updatePersonLocation(personId, request)

        // Assert
        verify(locationService).removeLocation(personId)
        verify(locationService).addLocation(newLocation)
    }

    @Test
    fun `getLocationsAround should call locationService with correct parameters`() {
        // Arrange
        val searchRequest = NearbySearchRequest(10.0, 20.0, 5.0)
        val mockLocations = listOf(mock<Location>(), mock<Location>())

        whenever(locationService.findAround(10.0, 20.0, 5.0)).thenReturn(mockLocations)

        // Act
        val result = service.getLocationsAround(searchRequest)

        // Assert
        assert(result.size == 2)
        assert(result.containsAll(mockLocations))
    }

    @Test
    fun `updatePersonLocation should throw exception and skip location logic when person is missing`() {
        // Arrange
        val personId = 99L
        val request = LocationRequest(30.0, 40.0)

        // Simulate the service failing to find the person
        whenever(personService.getById(personId))
            .thenThrow(NoSuchElementException("Person not found"))

        // Act & Assert
        val exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException::class.java) {
            service.updatePersonLocation(personId, request)
        }

        assert(exception.message == "Person not found")

        // VERIFY: These should NEVER be called if the person is missing
        verify(locationService, never()).removeLocation(mockAny())
        verify(locationService, never()).addLocation(mockAny())
        verify(locationMapper, never()).toLocation(mockAny(), mockAny())
    }

    @Test
    fun `registerNewPerson should fail if mapper throws exception`() {

        // Arrange
        val golfHobby = mutableSetOf(Hobby(0, "Golf"))
        val request = PersonRequest("John", "Dev", listOf("Golf"), 10.0, 20.0)

        whenever(personMapper.toPersonEntity(request, golfHobby))
            .thenThrow(IllegalArgumentException("Invalid name"))

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException::class.java) {
            service.registerNewPerson(request)
        }

        // VERIFY: The save was never reached
        verify(personService, never()).save(mockAny())
    }

}