package com.example.starwars.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.starwars.ui.screen.component.TopAppBar

@Composable
fun SetupNavGraph(
    startDestination: String,
    navController: NavHostController,
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                currentDestination = currentDestination,
                onNavigateBack = { navController.navigateUp() },
                onMenuClick = { },
            )
        }
    ) { innerPadding ->
        StarWarsNavHost(
            startDestination = startDestination,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
