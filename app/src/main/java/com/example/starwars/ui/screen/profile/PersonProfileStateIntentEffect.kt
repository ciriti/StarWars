package com.example.starwars.ui.screen.profile

import com.example.starwars.domain.model.PersonProfile

sealed class PersonProfileScreenState {
    data object Loading : PersonProfileScreenState()
    data class Success(val personProfile: PersonProfile) : PersonProfileScreenState()
    data class Error(val message: String) : PersonProfileScreenState()
}

sealed class PersonProfileScreenEffect {
    data class ShowError(val message: String) : PersonProfileScreenEffect()
}

sealed class PersonProfileScreenIntent {
    data class LoadPersonProfile(val id: Int) : PersonProfileScreenIntent()
}
