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
        val viewModel: PeopleViewModel = koinViewModel()
        PeopleScreen(
            viewModel = viewModel,
            onNavigateToDetails = { personId ->
                navController.navigate("person_profile/$personId")
            },
        )
    }
}
