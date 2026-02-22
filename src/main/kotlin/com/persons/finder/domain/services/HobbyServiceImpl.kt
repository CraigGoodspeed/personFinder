package com.persons.finder.domain.services

import com.persons.finder.data.Hobby
import com.persons.finder.repository.HobbyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service



@Service
class HobbyServiceImpl @Autowired constructor(
    val hobbyRepository: HobbyRepository,
    private val cacheManager: CacheManager
): HobbyService {

    override fun getByName(name: String) : Hobby? {
        return hobbyRepository.findByName(name)
    }

    override fun findAll(): List<Hobby> {
        return hobbyRepository.findAll()
    }

    override fun getOrCreateHobby(name: String): Hobby {
        val cache = cacheManager.getCache("hobbies")

        // 1. Atomic check and put
        return cache?.get(name, Hobby::class.java) ?: synchronized(this) {
            // 2. Double-check lock pattern
            hobbyRepository.findByName(name) ?: hobbyRepository.save(Hobby(name = name))
        }.also {
            cache?.put(name, it)
        }
    }
}