package com.example.starwars.ui.screen.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.starwars.ui.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    currentDestination: String?,
    onNavigateBack: () -> Unit,
    onMenuClick: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = when (currentDestination) {
                    Route.People.route -> "People"
                    else -> "Profile"
                }
            )
        },
        navigationIcon = {
            if (currentDestination != Route.People.route) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu"
                    )
                }
            }
        },
        actions = { }
    )
}

@Preview
@Composable
fun PreviewTopAppBar() {
    TopAppBar(
        currentDestination = Route.People.route,
        onNavigateBack = { },
        onMenuClick = { },
    )
}
