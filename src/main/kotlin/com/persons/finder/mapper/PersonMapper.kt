package com.persons.finder.mapper

import com.persons.finder.data.Hobby
import com.persons.finder.data.Location
import com.persons.finder.data.Person
import com.persons.finder.dto.PersonRequest

import org.springframework.stereotype.Component

@Component
class PersonMapper {

    // Converts the Request DTO to a Person Entity
    fun toPersonEntity(request: PersonRequest, hobbies: MutableSet<Hobby>): Person {
        return Person(
            name = request.name,
            hobbies = hobbies,
            jobTitle = request.jobTitle,
        )
    }

    // Converts the Request DTO and the saved Person into a Location Entity
    fun toLocationEntity(request: PersonRequest, savedPerson: Person): Location {
        return Location(
            latitude = request.latitude,
            longitude = request.longitude,
            person = savedPerson
        )
    }
}