package com.example.starwars.ui.screen.people


import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.starwars.ui.screen.component.ErrorMessage
import com.example.starwars.ui.screen.component.LoadingIndicator
import com.example.starwars.ui.screen.component.PersonItem

@Composable
fun PeopleScreen(
    viewModel: PeopleViewModel,
    modifier: Modifier = Modifier
        .semantics { contentDescription = "People" },
    onNavigateToDetails: (Int) -> Unit,
) {

    val state by viewModel.state.collectAsState()

    val peopleList by remember{
        derivedStateOf { (state as? PeopleScreenState.Success)?.characters ?: emptyList() }
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
        else -> LazyColumn(
            state = listState,
            modifier = modifier
        ) {
            items(items = peopleList, key = { it.id }) { person ->
                PersonItem(person = person) { onNavigateToDetails(person.id) }
            }
        }


    }
}
