package com.example.starwars.data.repository

import com.example.starwars.data.datasource.local.dao.PaginatedPersonDao
import com.example.starwars.data.datasource.local.dao.PersonDao
import com.example.starwars.data.datasource.local.entity.PaginatedPersonEntity
import com.example.starwars.data.datasource.local.entity.toDtoK
import com.example.starwars.data.datasource.remote.StarWarsApiClient
import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDtoK
import com.example.starwars.data.model.toEntity
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.repository.PeopleRepositoryK

fun PeopleRepository.Companion.createKtor(
    apiClient: StarWarsApiClient,
    personDao: PersonDao,
    paginatedPersonDao: PaginatedPersonDao,
): PeopleRepositoryK =
    PeopleRepositoryKtorImpl(apiClient, personDao, paginatedPersonDao)

internal class PeopleRepositoryKtorImpl(
    private val apiClient: StarWarsApiClient,
    private val personDao: PersonDao,
    private val paginatedPersonDao: PaginatedPersonDao,
    private val isExpired: (Long, Long) -> Boolean = { currentTime, cachedTime ->
        (currentTime - cachedTime) > 2 * 60 * 1000 // 2 minutes
    }
) : PeopleRepositoryK {

    override suspend fun getPeople(page: Int): Result<PaginatedResponse<PersonDtoK>> = runCatching {
        val currentTime = System.currentTimeMillis()

        val cachedPage = paginatedPersonDao.getPaginatedPerson(page)
        if (cachedPage != null && !isExpired(currentTime, cachedPage.timestamp)) {
            return@runCatching PaginatedResponse(
                count = cachedPage.count,
                next = cachedPage.next,
                previous = cachedPage.previous,
                results = cachedPage.results.map { it.toDtoK() }
            )
        }

        val result = apiClient.getPeople(page)
        result.onSuccess { remotePeople ->
            val personEntities = remotePeople.results.map { it.toEntity() }
            paginatedPersonDao.insertPaginatedPerson(
                PaginatedPersonEntity(
                    page = page,
                    count = remotePeople.count,
                    next = remotePeople.next,
                    previous = remotePeople.previous,
                    results = personEntities
                )
            )
        }
        result.getOrThrow()
    }

    override suspend fun getPersonById(id: Int): Result<PersonDtoK> = runCatching {
        val currentTime = System.currentTimeMillis()

        val cachedPerson = personDao.getPersonById(id)
        if (cachedPerson != null && !isExpired(currentTime, cachedPerson.timestamp)) {
            return@runCatching cachedPerson.toDtoK()
        }

        val result = apiClient.getPersonById(id)
        result.onSuccess { remotePerson ->
            personDao.insertPerson(remotePerson.toEntity())
        }
        result.getOrThrow()
    }
}
