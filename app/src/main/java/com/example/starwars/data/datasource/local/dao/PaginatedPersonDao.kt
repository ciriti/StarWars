package com.example.starwars.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.starwars.data.datasource.local.entity.PaginatedPersonEntity

@Dao
interface PaginatedPersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaginatedPerson(paginatedPerson: PaginatedPersonEntity)

    @Query("SELECT * FROM paginated_person WHERE page = :page")
    suspend fun getPaginatedPerson(page: Int): PaginatedPersonEntity?
}
