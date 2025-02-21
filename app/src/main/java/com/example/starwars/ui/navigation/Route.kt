package com.example.starwars.ui.navigation

sealed class Route(val route: String) {
    data object People : Route("people")
    data object PersonProfile : Route("person_profile") {
        fun passPersonId(personId: Int): String {
            return "person_profile/$personId"
        }
    }
}
