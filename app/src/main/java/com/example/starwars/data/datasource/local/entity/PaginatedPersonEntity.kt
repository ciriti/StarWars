package com.example.starwars.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "paginated_person")
@TypeConverters(Converters::class)
data class PaginatedPersonEntity(
    @PrimaryKey val page: Int,
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PersonEntity>,
    val timestamp: Long = System.currentTimeMillis()
)
