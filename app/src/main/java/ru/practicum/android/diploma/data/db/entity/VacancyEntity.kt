package ru.practicum.android.diploma.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancy_table")
data class VacancyEntity(
    @PrimaryKey(autoGenerate = true)
    val idDb: Int = 0,
    val id: String,
    val idAreaModel: String?,
    val nameAreaModel: String?,
    val countryId: String?,
    val idEmployerModel: String?,
    val original: String?,
    val logo90: String?,
    val logo240: String?,
    val nameEmployerModel: String?,
    val trusted: Boolean?,
    val url: String?,
    val vacanciesUrl: String?,
    val name: String?,
    val currency: String?,
    val from: Int?,
    val gross: Boolean?,
    val to: Int?,
    val description: String?,
    val idEmploymentModel: String?,
    val nameEmploymentModel: String?,
    val idExperienceModel: String?,
    val nameExperienceModel: String?,
    val email: String?,
    val nameContactsModel: String?,
    val idScheduleModel: String?,
    val nameScheduleModel: String?,
    val alternateUrl: String?
)
