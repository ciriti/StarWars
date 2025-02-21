package com.example.starwars.data.model

import com.example.starwars.data.datasource.local.entity.PersonEntity
import com.example.starwars.domain.model.Page
import com.example.starwars.domain.model.Person
import com.example.starwars.domain.model.PersonProfile

fun PersonDto.toPerson(): Person {
    val id = url.trim().split("/")[5].toIntOrNull()
        ?: throw IllegalArgumentException("Invalid person id")
    return Person(
        id = id,
        name = name,
        height = height,
        mass = mass,
        birthYear = birth_year,
        gender = gender,
    )
}

fun PersonDto.toPersonProfile(): PersonProfile {
    val id = url.trim().split("/")[5].toIntOrNull()
        ?: throw IllegalArgumentException("Invalid person id")
    return PersonProfile(
        id = id,
        name = name,
        height = height,
        mass = mass,
        hairColor = hair_color,
        skinColor = skin_color,
        eyeColor = eye_color,
        birthYear = birth_year,
        gender = gender,
        homeworld = homeworld,
        created = created.toString(),
        edited = edited.toString(),
        url = url,
    )
}

fun PaginatedResponse<PersonDto>.toPage(): Page<Person> = Page(
    count = count,
    next = next,
    previous = previous,
    results = results
        .map { runCatching { it.toPerson() } }
        .filter { it.isSuccess }
        .mapNotNull { it.getOrNull() }
)

fun PersonDto.toEntity(): PersonEntity {
    val id = url.trim().split("/")[5].toIntOrNull()
        ?: throw IllegalArgumentException("Invalid person id")
    return PersonEntity(
        id = id,
        name = name,
        height = height,
        mass = mass,
        hairColor = hair_color,
        skinColor = skin_color,
        eyeColor = eye_color,
        birthYear = birth_year,
        gender = gender,
        homeworld = homeworld,
        films = films,
        species = species,
        vehicles = vehicles,
        starships = starships,
        created = created,
        edited = edited,
        url = url
    )
}
