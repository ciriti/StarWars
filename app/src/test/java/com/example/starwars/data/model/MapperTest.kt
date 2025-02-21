package com.example.starwars.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.Date

class MapperTest {

    private val personDto = PersonDto(
        name = "Luke Skywalker",
        height = "172",
        mass = "77",
        hair_color = "blond",
        skin_color = "fair",
        eye_color = "blue",
        birth_year = "19BBY",
        gender = "male",
        homeworld = "",
        films = emptyList(),
        species = emptyList(),
        vehicles = emptyList(),
        starships = emptyList(),
        created = Date(),
        edited = Date(),
        url = "http://swapi.dev/api/people/1/"
    )

    @Test
    fun `toPerson should convert PersonDto to Person correctly`() {

        val person = personDto.toPerson()

        assertEquals(1, person.id)
        assertEquals("Luke Skywalker", person.name)
        assertEquals("172", person.height)
        assertEquals("77", person.mass)
        assertEquals("19BBY", person.birthYear)
        assertEquals("male", person.gender)
    }

    @Test
    fun `toPerson should throw IllegalArgumentException for invalid id`() {
        assertThrows(IllegalArgumentException::class.java) {
            personDto.copy(url = "http://swapi.dev/api/people/invalid/").toPerson()
        }
    }

    @Test
    fun `toPage should convert PaginatedResponse to Page correctly`() {
        val paginatedResponse = PaginatedResponse(
            count = 1,
            next = null,
            previous = null,
            results = listOf(personDto)
        )

        val page = paginatedResponse.toPage()

        assertEquals(1, page.count)
        assertEquals(null, page.next)
        assertEquals(null, page.previous)
        assertEquals(1, page.results.size)
        assertEquals("Luke Skywalker", page.results[0].name)
    }
}
