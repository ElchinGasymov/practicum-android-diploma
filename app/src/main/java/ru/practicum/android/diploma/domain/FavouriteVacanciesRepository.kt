package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface FavouriteVacanciesRepository {
    suspend fun getVacancy(vacancyId: String): VacancyDetails?

    suspend fun addVacancy(vacancy: VacancyDetails)

    suspend fun deleteVacancy(vacancyId: String)

    suspend fun hasLike(vacancyId: String): Boolean

    fun listVacancies(): Flow<List<Vacancy>>
}
