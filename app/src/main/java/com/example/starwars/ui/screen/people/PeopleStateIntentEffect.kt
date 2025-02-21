package com.example.starwars.ui.screen.people

import com.example.starwars.domain.model.Person

sealed interface PeopleScreenState {
    data object Loading : PeopleScreenState
    data class Error(val message: String) : PeopleScreenState
    data class Success(
        val characters: List<Person> = emptyList()
    ) : PeopleScreenState
}

sealed interface PeopleScreenEffect {
    data class ShowError(val message: String) : PeopleScreenEffect
    data class NavigateToPersonDetails(val personId: Int) : PeopleScreenEffect
}

sealed interface PeopleScreenIntent {
    data object LoadPeople : PeopleScreenIntent
    data class SelectPerson(val personId: Int) : PeopleScreenIntent
}
