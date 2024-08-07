package ru.practicum.android.diploma.domain.db

import ru.practicum.android.diploma.domain.models.VacancyDetails
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface SelectedVacanciesRepository {
    suspend fun getVacancy(vacancyId: Int): VacancyDetails

    suspend fun addVacancy(vacancy: VacancyDetails)

    suspend fun deleteVacancy(vacancyId: Int)

    suspend fun hasLike(vacancyId: Int):Boolean

    fun listVacancies(): Flow<List<Vacancy>>
}
