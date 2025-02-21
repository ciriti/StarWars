package com.example.starwars.data.datasource.remote

import com.example.starwars.data.model.PaginatedResponse
import com.example.starwars.data.model.PersonDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StarWarsApiService {
    @GET("people/")
    suspend fun getPeople(@Query("page") page: Int): Response<PaginatedResponse<PersonDto>>

    @GET("people/{id}/")
    suspend fun getPersonById(@Path("id") id: Int): Response<PersonDto>
}
