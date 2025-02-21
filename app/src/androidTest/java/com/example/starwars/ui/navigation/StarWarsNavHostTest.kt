package com.example.starwars.ui.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StarWarsNavHostTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            StarWarsNavHost(
                startDestination = Route.People.route,
                navController = navController
            )
        }
    }

    @Test
    fun verifyStartDestination() {
        composeTestRule
            .onNodeWithContentDescription("People")
            .assertIsDisplayed()
    }

    @Test
    fun verifyProfileDestination() {
        composeTestRule.runOnUiThread {
            navController.navigate(Route.PersonProfile.passPersonId(1))
        }

        composeTestRule.waitForIdle()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals("person_profile/{personId}", route)
    }
}
