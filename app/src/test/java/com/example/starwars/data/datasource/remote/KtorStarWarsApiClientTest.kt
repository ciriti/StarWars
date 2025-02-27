package com.example.starwars.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue

class KtorStarWarsApiClientTest {

    private fun clientFactory(mockEngine: MockEngine) = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
    }

    @Test
    fun `test getPeople returns successful result`() = runBlocking {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"count": 1, "next": null, "previous": null, "results": [{"name": "Luke Skywalker", "height": "172", "mass": "77", "hair_color": "blond", "skin_color": "fair", "eye_color": "blue", "birth_year": "19BBY", "gender": "male", "homeworld": "", "films": [], "species": [], "vehicles": [], "starships": [], "created": "2014-12-09T13:50:51.644000Z", "edited": "2014-12-20T21:17:56.891000Z", "url": "http://swapi.dev/api/people/1/"}]}""",
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
            )
        }

        val client = clientFactory(mockEngine)

        val apiClient = KtorStarWarsApiClient(client)
        val result = apiClient.getPeople(1)

        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals(1, response?.count)
        assertEquals("Luke Skywalker", response?.results?.firstOrNull()?.name)
    }

    @Test
    fun `test getPersonById returns successful result`() = runBlocking {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"name": "Luke Skywalker", "height": "172", "mass": "77", "hair_color": "blond", "skin_color": "fair", "eye_color": "blue", "birth_year": "19BBY", "gender": "male", "homeworld": "", "films": [], "species": [], "vehicles": [], "starships": [], "created": "2014-12-09T13:50:51.644000Z", "edited": "2014-12-20T21:17:56.891000Z", "url": "http://swapi.dev/api/people/1/"}""",
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
            )
        }

        val client = clientFactory(mockEngine)

        val apiClient = KtorStarWarsApiClient(client)
        val result = apiClient.getPersonById(1)

        assertTrue(result.isSuccess)
        val person = result.getOrNull()
        assertNotNull(person)
        assertEquals("Luke Skywalker", person?.name)
    }
}
