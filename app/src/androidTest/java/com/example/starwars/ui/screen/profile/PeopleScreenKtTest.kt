package com.example.starwars.ui.screen.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.starwars.domain.model.Person
import com.example.starwars.ui.screen.people.PeopleScreen
import com.example.starwars.ui.screen.people.PeopleScreenState
import com.example.starwars.ui.screen.people.PeopleViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PeopleScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var viewModel: PeopleViewModel

    @Before
    fun setUp() {
        viewModel = mockk(relaxed = true)
    }

    @Test
    fun loaderIndicatorIsDisplayed() {
        // arrange
        every { viewModel.state } returns MutableStateFlow(PeopleScreenState.Loading)
            .asStateFlow()

        // act
        composeTestRule.setContent {
            PeopleScreen(
                viewModel = viewModel,
                onNavigateToDetails = { }
            )
        }

        // assert
        composeTestRule.onNodeWithTag("LoadingIndicator").assertExists()
    }

    @Test
    fun errorMessageIsDisplayed() {
        // arrange
        every { viewModel.state } returns MutableStateFlow(PeopleScreenState.Error("error")).asStateFlow()

        // act
        composeTestRule.setContent {
            PeopleScreen(
                viewModel = viewModel,
                onNavigateToDetails = {}
            )
        }

        // assert
        composeTestRule.onNodeWithTag("ErrorMessageTag").assertExists()
        composeTestRule.onNodeWithText("Error: error").assertExists()
    }

    @Test
    fun onNavigateToDetailsIsInvoked(){
        // arrange
        val successState = PeopleScreenState.Success(listOf(mockPerson))
        every { viewModel.state } returns MutableStateFlow(successState)
        var selectedId = -1

        // act
        composeTestRule.setContent { PeopleScreen(viewModel) { selectedId = it } }
        composeTestRule.onNodeWithText("Luke Skywalker").performClick()

        // assert
        assertEquals(1, selectedId)
    }

    val mockPerson = Person(
        name = "Luke Skywalker",
        height = "172",
        mass = "77",
        birthYear = "19BBY",
        id = 1,
        gender = "MAle"
    )

}
