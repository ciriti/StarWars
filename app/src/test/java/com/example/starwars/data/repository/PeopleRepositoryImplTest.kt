package com.example.starwars.data.repository

import com.example.starwars.data.datasource.local.dao.PaginatedPersonDao
import com.example.starwars.data.datasource.local.dao.PersonDao
import com.example.starwars.data.datasource.local.entity.PaginatedPersonEntity
import com.example.starwars.data.datasource.remote.StarWarsApiService
import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDto
import com.example.starwars.domain.repository.PeopleRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PeopleRepositoryImplTest {

    private val mockApiService: StarWarsApiService = mockk()
    private val personDao: PersonDao = mockk()
    private val paginatedPersonDao: PaginatedPersonDao = mockk()
    private val peopleRepository: PeopleRepository = PeopleRepositoryImpl(
        mockApiService,
        personDao,
        paginatedPersonDao,
        isExpired = { _, _ -> true }
    )

    private val personId = 1
    lateinit var personDto: PersonDto

    @Before
    fun setup() {
        personDto = mockk(relaxed = true)
        every { personDto.url } returns "http://swapi.dev/api/people/1/"
    }

    @Test
    fun `getPersonById should return person when API call is successful`() = runBlocking {
        // Arrange
        coEvery { mockApiService.getPersonById(any()) } returns Response.success(personDto)
        coEvery { personDao.getPersonById(any()) } returns null
        coEvery { personDao.insertPerson(any()) } returns Unit

        // Act
        val result = peopleRepository.getPersonById(personId)

        // Assert
        assertEquals(Result.success(personDto), result)
        coVerify { mockApiService.getPersonById(personId) }
    }

    @Test
    fun `getPersonById should return an error when API call fails`() = runBlocking {
        // Arrange
        coEvery { mockApiService.getPersonById(personId) } returns Response.error(
            404,
            mockk(relaxed = true)
        )

        // Act
        val result = peopleRepository.getPersonById(personId)

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `getPeople should return paginated response when API call is successful`() = runBlocking {
        // Arrange
        val paginatedResponse = PaginatedResponse(
            count = 1,
            next = null,
            previous = null,
            results = listOf(personDto)
        )
        coEvery { mockApiService.getPeople(1) } returns Response.success(paginatedResponse)
        coEvery { paginatedPersonDao.getPaginatedPerson(any()) } returns null
        coEvery { paginatedPersonDao.insertPaginatedPerson(any()) } returns Unit

        // Act
        val result = peopleRepository.getPeople(1)

        // Assert
        assertEquals(Result.success(paginatedResponse), result)
        coVerify { mockApiService.getPeople(1) }
    }

    @Test
    fun `getPeople should return an error when API call fails`() = runBlocking {
        // Arrange
        coEvery { mockApiService.getPeople(1) } returns Response.error(404, mockk(relaxed = true))

        // Act
        val result = peopleRepository.getPeople(1)

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `getPeople should invoke paginatedPersonDao when cache is not expired`() = runBlocking {
        // Arrange
        val page = 1
        val cachedPage = mockk<PaginatedPersonEntity>()
        coEvery { paginatedPersonDao.getPaginatedPerson(page) } returns cachedPage
        val peopleRepository: PeopleRepository = PeopleRepositoryImpl(
            mockApiService,
            personDao,
            paginatedPersonDao,
            isExpired = { _, _ -> false }
        )

        // Act
        peopleRepository.getPeople(page)

        // Assert
        coVerify(exactly = 1) { paginatedPersonDao.getPaginatedPerson(page) }
    }
}
