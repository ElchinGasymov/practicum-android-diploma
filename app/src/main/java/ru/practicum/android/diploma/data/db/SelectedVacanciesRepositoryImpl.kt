package ru.practicum.android.diploma.data.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.db.SelectedVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class SelectedVacanciesRepositoryImpl(
    private val vacanciesDatabase: AppDatabase
) : SelectedVacanciesRepository {
    override suspend fun getVacancy(vacancyId: Int): VacancyDetails {
        return vacanciesDatabase.vacancyDao().findVacancy(vacancyId).map {}
    }

    override suspend fun addVacancy(vacancy: VacancyDetails) {
        vacanciesDatabase.vacancyDao().insertVacancy(vacancy.map {})
    }

    override suspend fun deleteVacancy(vacancyId: Int) {
        vacanciesDatabase.vacancyDao().deleteVacancy(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> = flow {
        vacanciesDatabase.vacancyDao().getSelectedVacancies()
        emit()
    }

    override suspend fun hasLike(vacancyId: Int): Boolean {
        return vacanciesDatabase.vacancyDao().hasLike(vacancyId) > 0
    }
}
