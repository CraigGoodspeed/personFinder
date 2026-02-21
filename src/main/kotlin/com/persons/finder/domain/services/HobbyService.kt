package com.persons.finder.domain.services

import com.persons.finder.data.Hobby

interface HobbyService {

    fun getByName(name:String) : Hobby?
    fun findAll() : List<Hobby>
}