package com.example.starwars.ui.screen.profile

import app.cash.turbine.turbineScope
import com.example.starwars.domain.model.PersonProfile
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

class PersonPersonProfileViewModelTest {

    private val mockPeopleService: PeopleService = mockk()
    private lateinit var viewModel: PersonProfileViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    lateinit var person: PersonProfile
    private val personId = 1

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PersonProfileViewModel(mockPeopleService)
        person = mockk(relaxed = true)
    }

    @Test
    fun `loadPersonProfile should update state to Success when service call is successful`() =
        testScope.runTest {
            turbineScope {
                // Arrange
                coEvery { mockPeopleService.getPersonById(any()) } returns Result.success(person)

                val states = viewModel.state.testIn(backgroundScope)

                // Act
                viewModel.loadPersonProfile(personId)

                // Assert
                assertEquals(PersonProfileScreenState.Loading, states.awaitItem())
                assertEquals(PersonProfileScreenState.Success(person), states.awaitItem())
            }
        }

    @Test
    fun `loadPersonProfile should update state to Error when service call fails`() =
        testScope.runTest {
            turbineScope {
                // Arrange
                val errorMessage = "Network error"
                coEvery { mockPeopleService.getPersonById(personId) } returns Result.failure(
                    Exception(
                        errorMessage
                    )
                )
                val states = viewModel.state.testIn(backgroundScope)

                // Act
                viewModel.loadPersonProfile(personId)

                // Assert
                assertEquals(PersonProfileScreenState.Loading, states.awaitItem())
                assertEquals(PersonProfileScreenState.Error(errorMessage), states.awaitItem())
            }
        }
}
