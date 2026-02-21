package com.persons.finder.domain.services

import com.persons.finder.data.Person

interface BioService {
    fun getBio(person: Person) : String

    fun buildSecurePrompt(job: String, hobbies: List<String>) : String
}