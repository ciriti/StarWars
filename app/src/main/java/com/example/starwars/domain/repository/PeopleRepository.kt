package com.example.starwars.domain.repository

import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDto
import com.example.starwars.data.model.PersonDtoK

interface PeopleRepository {
    suspend fun getPeople(page: Int): Result<PaginatedResponse<PersonDto>>
    suspend fun getPersonById(id: Int): Result<PersonDto>

    companion object
}

interface PeopleRepositoryK {
    suspend fun getPeople(page: Int): Result<PaginatedResponse<PersonDtoK>>
    suspend fun getPersonById(id: Int): Result<PersonDtoK>

    companion object
}
