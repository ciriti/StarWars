package com.example.starwars.di

import android.app.Application
import androidx.room.Room
import com.example.starwars.data.datasource.remote.StarWarsApiService
import com.example.starwars.data.datasource.remote.StarWarsNetworkClient
import com.example.starwars.data.datasource.local.StarWarsDatabase
import com.example.starwars.data.repository.create
import com.example.starwars.data.service.create
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.service.PeopleService
import com.example.starwars.ui.screen.people.PeopleViewModel
import com.example.starwars.ui.screen.profile.PersonProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // DB
    single {
        Room.databaseBuilder(get<Application>(), StarWarsDatabase::class.java, "starwars_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    //DAO
    single { get<StarWarsDatabase>().personDao() }
    single { get<StarWarsDatabase>().paginatedPersonDao() }

    // Data
    single<StarWarsApiService> { StarWarsNetworkClient.apiService }
    single<PeopleRepository> { PeopleRepository.create(get(), get(), get()) }
    single<PeopleService> { PeopleService.create(get()) }

    // ViewModels
    viewModel { PeopleViewModel(get()) }
    viewModel { PersonProfileViewModel(get()) }

}
