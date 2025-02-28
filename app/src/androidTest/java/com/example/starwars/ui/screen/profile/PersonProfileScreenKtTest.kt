package com.example.starwars.ui.screen.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.starwars.domain.model.PersonProfile
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PersonProfileScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private lateinit var viewModel: PersonProfileViewModel
    private lateinit var profile: PersonProfile

    @Before
    fun setUp(){
        viewModel = mockk(relaxed = true)
        profile = mockk(relaxed = true)
    }

    @Test
    fun testPersonProfileScreen(){
        // arrange
        val successState = PersonProfileScreenState.Success(profile)
        every { profile.id } returns 1
        every { profile.name } returns "Luke Skywalker"
        every { viewModel.state } returns MutableStateFlow(successState)

        // act
        composeTestRule.setContent {
            PersonProfileScreen(viewModel = viewModel, personId = 1)
        }

        // assert
        composeTestRule.onNodeWithText("Luke Skywalker").assertExists()
    }

}
