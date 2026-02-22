package com.persons.finder.events.handler

import com.persons.finder.domain.services.BioService
import com.persons.finder.domain.services.PersonsService
import com.persons.finder.events.model.PersonCreatedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class PersonCreatedHandler (val personService: PersonsService, val bioService: BioService) {

    @Async
    @EventListener
    fun handlePersonCreated(event: PersonCreatedEvent) {
        val person = personService.getById(event.personId)
        val newBio =
            bioService.getBio(person)
        person.extremelyFunkyBio = newBio
        personService.save(person)
    }
}