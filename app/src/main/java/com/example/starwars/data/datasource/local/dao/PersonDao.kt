package com.example.starwars.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starwars.data.datasource.local.entity.FilmEntity
import com.example.starwars.data.datasource.local.entity.PersonEntity

@Dao
interface PersonDao {
    @Query("SELECT * FROM person WHERE id = :id")
    suspend fun getPersonById(id: Int): PersonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: PersonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: FilmEntity)

    @Query("SELECT * FROM film WHERE personId = :personId")
    suspend fun getFilmByPersonId(personId: Int) : List<FilmEntity>
}
