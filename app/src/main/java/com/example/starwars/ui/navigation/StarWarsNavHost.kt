package com.example.starwars.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.starwars.ui.screen.people.peopleRoute
import com.example.starwars.ui.screen.profile.personProfileRoute

@Composable
fun StarWarsNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
) {
    NavHost(
        startDestination = startDestination,
        navController = navController,
        modifier = modifier
    ) {
        peopleRoute(navController)
        personProfileRoute()
    }
}
