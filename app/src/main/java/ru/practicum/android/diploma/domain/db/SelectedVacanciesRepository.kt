package ru.practicum.android.diploma.domain.db

import java.util.concurrent.Flow

interface SelectedVacanciesRepository {
    suspend fun getVacancy(vacancyId: Long): Vacancy

    suspend fun addVacancy(vacancy: Vacancy)

    suspend fun deleteVacancy(vacancyId: Long)

    fun listVacancies(): Flow<List<Vacancy>>
}
