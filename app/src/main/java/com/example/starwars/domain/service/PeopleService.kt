package com.example.starwars.domain.service

import com.example.starwars.domain.model.Page
import com.example.starwars.domain.model.Person
import com.example.starwars.domain.model.PersonProfile

interface PeopleService {
    suspend fun getPeople(page: Int): Result<Page<Person>>
    suspend fun getPersonById(id: Int): Result<PersonProfile>

    companion object
}
