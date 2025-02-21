package com.example.starwars.data.service

import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDto
import com.example.starwars.data.model.toPage
import com.example.starwars.data.model.toPerson
import com.example.starwars.data.model.toPersonProfile
import com.example.starwars.domain.model.Page
import com.example.starwars.domain.model.Person
import com.example.starwars.domain.model.PersonProfile
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.service.PeopleService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun PeopleService.Companion.create(peopleRepository: PeopleRepository): PeopleService =
    PeopleServiceImpl(peopleRepository)

internal class PeopleServiceImpl(
    private val peopleRepository: PeopleRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PeopleService {

    override suspend fun getPeople(page: Int): Result<Page<Person>> =
        withContext(ioDispatcher) {
            runCatching {
                val peopleDto: PaginatedResponse<PersonDto> =
                    peopleRepository.getPeople(page).getOrElse { throw it }
                peopleDto.toPage()
            }
        }


    override suspend fun getPersonById(id: Int): Result<PersonProfile> =
        withContext(ioDispatcher) {
            runCatching {
                val personDto = peopleRepository.getPersonById(id).getOrElse { throw it }
                personDto.toPersonProfile()
            }
        }
}
