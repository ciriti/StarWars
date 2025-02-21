package com.example.starwars.data.service

import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDto
import com.example.starwars.data.model.toPage
import com.example.starwars.data.model.toPersonProfile
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.service.PeopleService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PeopleServiceImplTest {

    private val mockPeopleRepository: PeopleRepository = mockk()
    private val peopleService: PeopleService = PeopleServiceImpl(
        mockPeopleRepository,
        Dispatchers.Unconfined
    )

    private val personId = 1
    lateinit var personDto: PersonDto

    @Before
    fun setup() {
        personDto = mockk(relaxed = true)
        every { personDto.url } returns "http://swapi.dev/api/people/1/"
    }

    @Test
    fun `getPersonById should return person when repository call is successful`() = runTest {
        // Arrange
        coEvery { mockPeopleRepository.getPersonById(personId) } returns Result.success(personDto)

        // Act
        val result = peopleService.getPersonById(personId)

        // Assert
        assertEquals(Result.success(personDto.toPersonProfile()), result)
        coVerify { mockPeopleRepository.getPersonById(personId) }
    }

    @Test
    fun `getPersonById should return an error when repository call fails`() = runTest {
        // Arrange
        val exception = RuntimeException("Repository error")
        coEvery { mockPeopleRepository.getPersonById(personId) } returns Result.failure(exception)

        // Act
        val result = peopleService.getPersonById(personId)

        // Assert
        assert(result.isFailure)
    }

    @Test
    fun `getPeople should return paginated response when repository call is successful`() =
        runTest {
            // Arrange
            val paginatedResponse = PaginatedResponse(
                count = 1,
                next = null,
                previous = null,
                results = listOf(personDto)
            )
            coEvery { mockPeopleRepository.getPeople(1) } returns Result.success(paginatedResponse)

            // Act
            val result = peopleService.getPeople(1)

            // Assert
            assertEquals(Result.success(paginatedResponse.toPage()), result)
            coVerify { mockPeopleRepository.getPeople(1) }
        }

    @Test
    fun `getPeople should return an error when repository call fails`() = runTest {
        // Arrange
        val exception = RuntimeException("Repository error")
        coEvery { mockPeopleRepository.getPeople(1) } returns Result.failure(exception)

        // Act
        val result = peopleService.getPeople(1)

        // Assert
        assert(result.isFailure)
    }
}
