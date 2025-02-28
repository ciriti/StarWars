package com.example.starwars.ui.screen.people


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.example.starwars.domain.model.Page
import com.example.starwars.domain.model.Person
import com.example.starwars.domain.model.PersonProfile
import com.example.starwars.domain.service.PeopleService
import com.example.starwars.ui.screen.component.ErrorMessage
import com.example.starwars.ui.screen.component.LoadingIndicator
import com.example.starwars.ui.screen.component.PersonItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun PeopleScreen(
    viewModel: PeopleViewModel = koinViewModel(),
    modifier: Modifier = Modifier
        .semantics { contentDescription = "People" },
    onNavigateToDetails: (Int) -> Unit,
) {

    val state by viewModel.state.collectAsState()

    val character by remember {
        derivedStateOf {
            (state as? PeopleScreenState.Success)?.characters ?: emptyList()
        }
    }

    val isLoading by remember {
        derivedStateOf { state is PeopleScreenState.Loading }
    }

    val errorMessage by remember {
        derivedStateOf { (state as? PeopleScreenState.Error)?.message }
    }

    val listState = rememberLazyListState()
    // Load more data when the last item becomes visible
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex >= listState.layoutInfo.totalItemsCount - 5) {
                    viewModel.loadData()
                }
            }
    }

    when {
        isLoading -> LoadingIndicator(modifier = modifier)

        errorMessage != null -> {
            ErrorMessage(
                message = errorMessage!!,
                onRetry = { viewModel.loadData() },
                modifier = modifier
            )
        }

        else -> {
            LazyColumn(
                state = listState,
                modifier = modifier
            ) {
                items(character) { person ->
                    PersonItem(person = person) { onNavigateToDetails(person.id) }
                }
            }
        }
    }
}


val mockPage = Page(
    next = null,
    previous = null,
    results = listOf(
        Person(1, "Luke Skywalker", "180", "80", "1980", "Male"),
        Person(2, "Luke Skywalker", "180", "80", "1980", "Male"),
        Person(3, "Luke Skywalker", "180", "80", "1980", "Male"),
    ),
    count = 1
)

class MockPeopleViewModel(
    val resultGetPeople: Result<Page<Person>>,
    val resultGetPersonId: Result<PersonProfile>
) : PeopleViewModel(
    peopleService = object : PeopleService {
        override suspend fun getPeople(page: Int): Result<Page<Person>> = resultGetPeople
        override suspend fun getPersonById(id: Int): Result<PersonProfile> = resultGetPersonId
    },
    getNextPage = { next -> null }
)

@Preview(name = "SuccessCase")
@Composable
fun PeopleScreenSuccessPreview() {
    PeopleScreen(
        viewModel = MockPeopleViewModel(
            resultGetPeople = Result.success(mockPage),
            resultGetPersonId = Result.failure(RuntimeException())
        ),
        onNavigateToDetails = {}
    )
}

@Preview(name = "ErrorCase")
@Composable
fun PeopleScreenErrorPreview() {
    MaterialTheme {
        PeopleScreen(
            viewModel = MockPeopleViewModel(
                resultGetPeople = Result.failure(RuntimeException()),
                resultGetPersonId = Result.failure(RuntimeException())
            ),
            onNavigateToDetails = {}
        )
    }
}
