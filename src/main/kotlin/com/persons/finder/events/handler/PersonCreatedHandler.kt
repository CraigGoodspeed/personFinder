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
        val newBio =
            bioService.getBio(event.person)
        event.person.extremelyFunkyBio = newBio
        personService.save(event.person)
    }
}