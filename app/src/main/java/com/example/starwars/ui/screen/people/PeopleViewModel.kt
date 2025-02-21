package com.example.starwars.ui.screen.people

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.domain.model.Page
import com.example.starwars.domain.model.Person
import com.example.starwars.domain.service.PeopleService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PeopleViewModel(
    private val peopleService: PeopleService,
    private val getNextPage: (String?) -> Int? = { next ->
        next?.let { Uri.parse(next).getQueryParameter("page")?.toIntOrNull() }
    }
) : ViewModel() {

    private val _state =
        MutableStateFlow<PeopleScreenState>(PeopleScreenState.Loading)
    val state: StateFlow<PeopleScreenState> = _state.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    fun loadData() {
        fetchPeople(currentPage)
    }

    private fun fetchPeople(page: Int) {
        if (isLoading || isLastPage) return
        viewModelScope.launch {
            isLoading = true
            peopleService.getPeople(page).fold(
                onFailure = { handleFailure(it) },
                onSuccess = { handleSuccess(it) }
            ).run { isLoading = false }
        }
    }

    private fun handleFailure(throwable: Throwable) {
        val errorMessage = throwable.message ?: "Unknown error"
        if (_state.value == PeopleScreenState.Loading) {
            _state.value = PeopleScreenState.Error(errorMessage)
        }
    }

    private fun handleSuccess(peoplePage: Page<Person>) {
        isLoading = false
        updateStateWithSuccess(peoplePage.results)
        getNextPage(peoplePage.next)?.let {
            currentPage = it
        } ?: run { isLastPage = true }
    }

    private fun updateStateWithSuccess(newResults: List<Person>) {
        _state.value = PeopleScreenState.Success(
            characters = _state.value.let { currentState ->
                if (currentState is PeopleScreenState.Success) {
                    currentState.characters + newResults
                } else {
                    newResults
                }
            }
        )
    }
}
