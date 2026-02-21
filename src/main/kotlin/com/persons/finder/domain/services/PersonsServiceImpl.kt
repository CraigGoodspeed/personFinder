package com.persons.finder.domain.services

import com.persons.finder.data.Person
import com.persons.finder.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PersonsServiceImpl @Autowired constructor(private val personRepository: PersonRepository): PersonsService {

    override fun getById(id: Long): Person {
        return personRepository.findById(id).orElseThrow {
            NoSuchElementException("Person with ID $id not found")
        }
    }

    override fun save(person: Person) {
        personRepository.save(person)
    }

}