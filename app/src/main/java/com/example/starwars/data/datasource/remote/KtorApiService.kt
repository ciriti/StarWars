package com.example.starwars.data.datasource.remote

import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDtoK
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


interface StarWarsApiClient {
    suspend fun getPeople(page: Int): Result<PaginatedResponse<PersonDtoK>>
    suspend fun getPersonById(id: Int): Result<PersonDtoK>
}

val httpClient = HttpClient(getHttpClientEngine()) {
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

fun getHttpClientEngine(): HttpClientEngineFactory<*> = Android

internal class KtorStarWarsApiClient(
    private val client: HttpClient = httpClient,
    private val baseUrl: String = "https://swapi.dev/api/",
) : StarWarsApiClient {

    override suspend fun getPeople(page: Int): Result<PaginatedResponse<PersonDtoK>> = runCatching {
        val response: HttpResponse = client.get("$baseUrl/people/") {
            parameter("page", page)
        }
        if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            throw Exception("Failed to fetch people")
        }
    }

    override suspend fun getPersonById(id: Int): Result<PersonDtoK> = runCatching {
        val response: HttpResponse = client.get("$baseUrl/people/$id/")
        if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            throw Exception("Failed to fetch person")
        }
    }
}
