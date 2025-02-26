package com.example.starwars.ui.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.starwars.domain.model.Page
import com.example.starwars.domain.model.Person
import com.example.starwars.domain.model.PersonProfile
import com.example.starwars.domain.service.PeopleService
import com.example.starwars.ui.screen.component.ErrorMessage
import com.example.starwars.ui.screen.component.LoadingIndicator
import com.example.starwars.ui.theme.StarWarsAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonProfileScreen(
    personId: Int,
    viewModel: PersonProfileViewModel = koinViewModel(),
    modifier: Modifier = Modifier
        .semantics { contentDescription = "Profile" },
) {
    val profileState by viewModel.state.collectAsState()

    LaunchedEffect(personId) {
        viewModel.loadPersonProfile(personId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (profileState) {
            is PersonProfileScreenState.Loading -> {
                LoadingIndicator(modifier = modifier)
            }

            is PersonProfileScreenState.Success -> {
                val person = (profileState as PersonProfileScreenState.Success).personProfile

                Text(
                    text = person.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )

                ProfileDetailSection(
                    details = listOf(
                        "Height" to person.height,
                        "Mass" to person.mass,
                        "Hair Color" to person.hairColor,
                        "Skin Color" to person.skinColor,
                        "Eye Color" to person.eyeColor,
                        "Birth Year" to person.birthYear,
                        "Gender" to person.gender,
                        "Homeworld" to person.homeworld,
                        "Created" to person.created,
                        "Edited" to person.edited,
                        "URL" to person.url
                    )
                )
            }


            is PersonProfileScreenState.Error -> {
                ErrorMessage(
                    message = (profileState as PersonProfileScreenState.Error).message,
                    modifier = modifier,
                    showRetryButton = false
                )
            }
        }
    }
}


@Composable
fun ProfileDetailSection(details: List<Pair<String, String>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            details.forEach { (label, value) ->
                Text(text = "$label: $value")
            }
        }
    }
}

val mockProfile = PersonProfile(
    id = 1,
    name = "Luke Skywalker",
    height = "180",
    mass = "80",
    url = "",
    hairColor = "hairColor",
    skinColor = "skinColor",
    eyeColor = "eyeColor",
    birthYear = "birthYear",
    homeworld = "homeworld",
    created = "created",
    edited = "edited",
    gender = "gender"
)

class MockViewModel(
    val resultGetPeople: Result<Page<Person>> = Result.failure(RuntimeException()),
    val resultGetPersonId: Result<PersonProfile> = Result.success(mockProfile)
) : PersonProfileViewModel(
    peopleService = object : PeopleService {
        override suspend fun getPeople(page: Int): Result<Page<Person>> = resultGetPeople
        override suspend fun getPersonById(id: Int): Result<PersonProfile> = resultGetPersonId
    }
)


@Preview(showBackground = true, name = "ScreenSuccessCase")
@Composable
fun PreviewProfileSuccessCase() {
    StarWarsAppTheme {
        PersonProfileScreen(
            personId = 1,
            viewModel = MockViewModel().apply { loadPersonProfile(1) },
        )
    }
}

@Preview(showBackground = true, name = "ScreenSuccessCase")
@Composable
fun PreviewProfileErrorCase() {
    StarWarsAppTheme {
        PersonProfileScreen(
            personId = 1,
            viewModel = MockViewModel(
                resultGetPersonId = Result.failure(RuntimeException("Error"))
            ).apply { loadPersonProfile(1) },
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileDetailSection() {
    StarWarsAppTheme {
        ProfileDetailSection(
            details = listOf(
                "Height" to "180 cm",
                "Mass" to "75 kg",
                "Hair Color" to "Brown",
                "Skin Color" to "Light",
                "Eye Color" to "Blue",
                "Birth Year" to "1990",
                "Gender" to "Male"
            )
        )
    }
}
