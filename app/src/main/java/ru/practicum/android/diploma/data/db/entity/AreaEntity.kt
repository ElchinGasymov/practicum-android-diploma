package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area_table")
data class AreaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val idVacancy: String,
    val idArea: String?,
    val name: String?,
    val countryId: String?,
)
