package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val nameEmployer: String,
    val logo240: String?,
    val currency: String?,
    val from: Int?,
    val to: Int?,
    val gross: Boolean?,
    val idArea: Int,
    val nameArea: String,
    val alternateUrl: String,
    val description: String,
    val idEmployment: String,
    val nameEmployment: String,
    val idExperience: String,
    val nameExperience: String,
    val idSchedule: String,
    val nameSchedule: String,
    val email: String,
    val nameContacts: String,
    val raw: String?
)
