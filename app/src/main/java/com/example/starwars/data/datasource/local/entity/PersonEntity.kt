package com.example.starwars.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.starwars.data.model.PersonDto
import com.example.starwars.data.model.PersonDtoK
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
    foreignKeys = [
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["personId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class FilmEntity(
    @PrimaryKey val personId : Int,
    val filmUrl: String
)

fun PersonEntity.toDto(filmEntries: List<FilmEntity> = emptyList()): PersonDto {
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
        // TODO create a list of FilmEntities
        films = films,
        species = species,
        vehicles = vehicles,
        starships = starships,
        created = created,
        edited = edited,
        url = url
    )
}

fun PersonEntity.toDtoK(): PersonDtoK {
    return PersonDtoK(
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
        created = Date().toString(),
        edited =  Date().toString(),
        url = url
    )
}
