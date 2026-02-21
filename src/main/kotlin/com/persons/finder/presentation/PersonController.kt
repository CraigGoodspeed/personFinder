package com.persons.finder.presentation

import com.persons.finder.data.Location
import com.persons.finder.dto.LocationRequest
import com.persons.finder.dto.NearbySearchRequest
import com.persons.finder.dto.PersonRequest
import com.persons.finder.facade.PersonManagementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/persons")
class PersonController @Autowired constructor(
    private val personManagementService: PersonManagementService,
) {


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPerson(@Valid @RequestBody request: PersonRequest) {
        personManagementService.registerNewPerson(request)
    }

    @PutMapping("/{id}/location")
    fun updateLocation(
        @PathVariable id: Long,
        @Valid @RequestBody request: LocationRequest
    ): ResponseEntity<String> {
        personManagementService.updatePersonLocation(
            id,
            request
        )
        return ResponseEntity.ok("Location updated successfully")
    }

    @GetMapping("/nearby")
    fun getNearby(@Valid @RequestBody request: NearbySearchRequest): List<Location> {
        return personManagementService.getLocationsAround(request)
    }

}