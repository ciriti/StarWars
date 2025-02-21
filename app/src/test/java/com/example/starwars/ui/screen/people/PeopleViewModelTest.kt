package com.example.starwars.ui.screen.people

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.starwars.domain.model.Page
import com.example.starwars.domain.model.Person
import com.example.starwars.domain.service.PeopleService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PeopleViewModelTest {

    private val mockPeopleService: PeopleService = mockk()
    private lateinit var viewModel: PeopleViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    lateinit var person: Person

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PeopleViewModel(peopleService = mockPeopleService, getNextPage = { 1 })
        person = mockk(relaxed = true)
    }

    @Test
    fun `loadData should update state to Success when service call is successful`() =
        testScope.runTest {
            turbineScope {
                // Arrange
                val peoplePage = Page(
                    count = 1,
                    next = null,
                    previous = null,
                    results = listOf(person)
                )
                coEvery { mockPeopleService.getPeople(1) } returns Result.success(peoplePage)
                val states = viewModel.state.testIn(backgroundScope)

                // Act
                viewModel.loadData()

                // Assert
                assertEquals(PeopleScreenState.Loading, states.awaitItem())
                assertEquals(PeopleScreenState.Success(peoplePage.results), states.awaitItem())
            }
        }

    @Test
    fun `loadData should update state to Error when service call fails`() = testScope.runTest {
        // Arrange
        val errorMessage = "Network error"
        coEvery { mockPeopleService.getPeople(1) } returns Result.failure(Exception(errorMessage))

        // Act & Assert
        viewModel.state.test {
            viewModel.loadData()

            assertEquals(PeopleScreenState.Loading, awaitItem())
            assertEquals(PeopleScreenState.Error(errorMessage), awaitItem())
        }
    }
}
