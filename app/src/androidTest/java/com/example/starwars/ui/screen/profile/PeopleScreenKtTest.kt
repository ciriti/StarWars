package com.example.starwars.ui.screen.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.starwars.domain.model.Person
import com.example.starwars.ui.screen.people.PeopleScreenContent
import com.example.starwars.ui.screen.people.PeopleScreenState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PeopleScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loaderIndicatorIsDisplayed() {
        // act
        composeTestRule.setContent {
            PeopleScreenContent(
                state = PeopleScreenState.Loading,
                onNavigateToDetails = { },
                loadNextPage = {}
            )
        }

        // assert
        composeTestRule.onNodeWithTag("LoadingIndicator").assertExists()
    }

    @Test
    fun errorMessageIsDisplayed() {
        // act
        composeTestRule.setContent {
            PeopleScreenContent(
                state = PeopleScreenState.Error("error"),
                onNavigateToDetails = { },
                loadNextPage = {}
            )
        }

        // assert
        composeTestRule.onNodeWithTag("ErrorMessageTag").assertExists()
        composeTestRule.onNodeWithText("Error: error").assertExists()
    }

    @Test
    fun onNavigateToDetailsIsInvoked() {
        // arrange
        var selectedId = -1

        // act
        composeTestRule.setContent { PeopleScreenContent(
            state = PeopleScreenState.Success(listOf(mockPerson)),
            onNavigateToDetails = { selectedId = it },
            loadNextPage = {}
        )  }
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
