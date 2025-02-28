package com.example.starwars.ui.screen.people

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.starwars.ui.navigation.Route
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.peopleRoute(
    navController: NavHostController,
) {
    composable(Route.People.route) {
        PeopleScreen(
            onNavigateToDetails = { personId ->
                navController.navigate("person_profile/$personId"){
                    popUpTo(Route.People.route) {
                        saveState = true
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            },
        )
    }
}
