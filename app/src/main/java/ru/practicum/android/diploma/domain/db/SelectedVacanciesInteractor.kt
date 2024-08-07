package ru.practicum.android.diploma.domain.db

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

interface SelectedVacanciesInteractor {
    suspend fun getVacancy(vacancyId: Int): VacancyDetails

    suspend fun addVacancy(vacancy: VacancyDetails)

    suspend fun deleteVacancy(vacancyId: Int)

    suspend fun hasLike(vacancyId: Int): Boolean

    fun listVacancies(): Flow<List<Vacancy>>
}
