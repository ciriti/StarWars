package com.example.starwars.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.starwars.data.datasource.local.dao.PaginatedPersonDao
import com.example.starwars.data.datasource.local.dao.PersonDao
import com.example.starwars.data.datasource.local.entity.Converters
import com.example.starwars.data.datasource.local.entity.PaginatedPersonEntity
import com.example.starwars.data.datasource.local.entity.PersonEntity

@Database(entities = [PersonEntity::class, PaginatedPersonEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class StarWarsDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun paginatedPersonDao(): PaginatedPersonDao
}
