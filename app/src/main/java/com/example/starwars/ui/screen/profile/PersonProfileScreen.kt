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
import com.example.starwars.domain.model.PersonProfile
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
    PersonProfileContent(profileState, modifier)
}

@Composable
fun PersonProfileContent(
    profileState: PersonProfileScreenState,
    modifier: Modifier = Modifier
        .semantics { contentDescription = "Profile" },
) {
    when (profileState) {
        is PersonProfileScreenState.Loading -> {
            LoadingIndicator(modifier = modifier)
        }

        is PersonProfileScreenState.Success -> {
            val person = profileState.personProfile
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
        }

        is PersonProfileScreenState.Error -> {
            ErrorMessage(
                message = profileState.message,
                modifier = modifier,
                showRetryButton = false
            )
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


@Preview(showBackground = true, name = "ScreenSuccessCase")
@Composable
fun PreviewProfileSuccessCase() {
    StarWarsAppTheme {
        PersonProfileContent(
            profileState = PersonProfileScreenState.Success(mockProfile)
        )
    }
}

@Preview(showBackground = true, name = "ScreenSuccessCase")
@Composable
fun PreviewProfileErrorCase() {
    StarWarsAppTheme {
        PersonProfileContent(
            profileState = PersonProfileScreenState.Error("Error")
        )
    }
}

@Preview(showBackground = true, name = "ScreenLoadingCase")
@Composable
fun PreviewProfileLoadingCase() {
    StarWarsAppTheme {
        PersonProfileContent(
            profileState = PersonProfileScreenState.Loading
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileDetailSection() {
    MaterialTheme {
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
