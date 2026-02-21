package com.persons.finder.domain.services

import com.persons.finder.data.Hobby
import com.persons.finder.repository.HobbyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service



@Service
class HobbyServiceImpl @Autowired constructor(val hobbyRepository: HobbyRepository): HobbyService {

    @Cacheable(value = ["hobbies"], key = "#name")
    override fun getByName(name: String) : Hobby? {
        return hobbyRepository.findByName(name)
    }

    override fun findAll(): List<Hobby> {
        return hobbyRepository.findAll()
    }
}