package com.persons.finder.repository

import com.persons.finder.data.Hobby
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HobbyRepository : JpaRepository<Hobby, Long> {
    fun findByName(name: String): Hobby?
}
