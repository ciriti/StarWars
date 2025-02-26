package com.example.starwars.ui.screen.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.starwars.ui.navigation.Route
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf


fun NavGraphBuilder.personProfileRoute() {
    composable(
        route = "${Route.PersonProfile.route}/{personId}",
        arguments = listOf(navArgument("personId") { defaultValue = -1 })
    ) { backStackEntry ->
        val personId = backStackEntry.arguments?.getInt("personId") ?: return@composable

        PersonProfileScreen(
            personId = personId,
            modifier = Modifier.fillMaxSize()
        )
    }
}
