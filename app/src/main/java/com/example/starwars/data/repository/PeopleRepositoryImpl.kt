package com.example.starwars.data.repository


import com.example.starwars.data.datasource.remote.StarWarsApiService
import com.example.starwars.data.datasource.local.dao.PaginatedPersonDao
import com.example.starwars.data.datasource.local.dao.PersonDao
import com.example.starwars.data.datasource.local.entity.PaginatedPersonEntity
import com.example.starwars.data.datasource.local.entity.toDto
import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDto
import com.example.starwars.data.model.toEntity
import com.example.starwars.domain.getException
import com.example.starwars.domain.repository.PeopleRepository
import kotlinx.coroutines.delay
import retrofit2.Response

fun PeopleRepository.Companion.create(
    apiService: StarWarsApiService,
    personDao: PersonDao,
    paginatedPersonDao: PaginatedPersonDao,
): PeopleRepository =
    PeopleRepositoryImpl(apiService, personDao, paginatedPersonDao)

internal class PeopleRepositoryImpl(
    private val apiService: StarWarsApiService,
    private val personDao: PersonDao,
    private val paginatedPersonDao: PaginatedPersonDao,
    private val exceptionMapper: (Response<*>) -> Exception = { r -> getException(r) },
    private val isExpired: (Long, Long) -> Boolean = { currentTime, cachedTime ->
        (currentTime - cachedTime) > 2 * 60 * 1000 // 2 minutes
    }
) : PeopleRepository {

    override suspend fun getPeople(page: Int): Result<PaginatedResponse<PersonDto>> = runCatching {
        val currentTime = System.currentTimeMillis()

        val cachedPage = paginatedPersonDao.getPaginatedPerson(page)
        if (cachedPage != null && !isExpired(currentTime, cachedPage.timestamp)) {
            return@runCatching PaginatedResponse(
                count = cachedPage.count,
                next = cachedPage.next,
                previous = cachedPage.previous,
                results = cachedPage.results.map { it.toDto() }
            )
        }

        val response = apiService.getPeople(page)
        // add more delay for show case purpose
        delay(200)
        if (response.isSuccessful) {
            val remotePeople = response.body() ?: throw Exception("Empty response")

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
            remotePeople
        } else {
            throw exceptionMapper(response)
        }
    }

    override suspend fun getPersonById(id: Int): Result<PersonDto> = runCatching {
        val currentTime = System.currentTimeMillis()

        val cachedPerson = personDao.getPersonById(id)
        if (cachedPerson != null && !isExpired(currentTime, cachedPerson.timestamp)) {
            return@runCatching cachedPerson.toDto()
        }

        val response = apiService.getPersonById(id)
        // add more delay for show case purpose
        delay(600)
        if (response.isSuccessful) {
            val remotePerson = response.body()!!
            personDao.insertPerson(remotePerson.toEntity())
            remotePerson
        } else {
            throw exceptionMapper(response)
        }
    }
}
