package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_table")
data class PhoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val idVacancy: Int,
    val cityCode: String?,
    val comment: String?,
    val countryCode: String?,
    val formatted: String?,
    val number: String?
)
