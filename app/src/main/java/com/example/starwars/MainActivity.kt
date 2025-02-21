package com.example.starwars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.starwars.ui.navigation.Route
import com.example.starwars.ui.navigation.SetupNavGraph
import com.example.starwars.ui.theme.StarWarsAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    startDestination = Route.People.route,
                    navController = navController
                )
            }
        }
    }
}
