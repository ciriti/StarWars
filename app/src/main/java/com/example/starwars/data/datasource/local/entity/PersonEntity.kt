package com.example.starwars.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.starwars.data.model.PersonDto
import java.util.Date

@Entity(tableName = "person")
@TypeConverters(Converters::class)
data class PersonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: String,
    val mass: String,
    val hairColor: String,
    val skinColor: String,
    val eyeColor: String,
    val birthYear: String,
    val gender: String,
    val homeworld: String,
    val films: List<String>,
    val species: List<String>,
    val vehicles: List<String>,
    val starships: List<String>,
    val created: Date,
    val edited: Date,
    val url: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "film",

)
data class FilmEntity(
    val personId : Int,
    val filmUrl: String
)

fun PersonEntity.toDto(): PersonDto {
    return PersonDto(
        name = name,
        height = height,
        mass = mass,
        hair_color = hairColor,
        skin_color = skinColor,
        eye_color = eyeColor,
        birth_year = birthYear,
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
