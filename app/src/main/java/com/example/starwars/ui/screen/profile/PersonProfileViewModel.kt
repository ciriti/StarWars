package com.example.starwars.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.domain.service.PeopleService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonProfileViewModel(
    private val peopleService: PeopleService,
) : ViewModel() {

    private val _state =
        MutableStateFlow<PersonProfileScreenState>(PersonProfileScreenState.Loading)
    val state = _state.asStateFlow()

    fun loadPersonProfile(id: Int) {
        viewModelScope.launch {
            val result = peopleService.getPersonById(id)
            result.onSuccess { person ->
                _state.value = PersonProfileScreenState.Success(person)
            }.onFailure { throwable ->
                _state.value = PersonProfileScreenState.Error(throwable.message ?: "Unknown error")
            }
        }
    }
}
