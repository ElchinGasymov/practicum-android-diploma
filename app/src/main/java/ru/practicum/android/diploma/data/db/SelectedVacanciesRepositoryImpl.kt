package ru.practicum.android.diploma.data.db

import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.db.SelectedVacanciesRepository
import java.util.concurrent.Flow

class SelectedVacanciesRepositoryImpl(
    private val vacanciesDatabase: AppDatabase
) : SelectedVacanciesRepository {
    override suspend fun getVacancy(vacancyId: Int): Vacancy {
        return vacanciesDatabase.vacancyDao().findVacancy(vacancyId).map {}
    }

    override suspend fun addVacancy(vacancy: Vacancy) {
        vacanciesDatabase.vacancyDao().insertVacancy(vacancy.map {})
    }

    override suspend fun deleteVacancy(vacancyId: Int) {
        vacanciesDatabase.vacancyDao().deleteVacancy(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> = flow {
        emit(vacanciesDatabase.vacancyDao().getSelectedVacancies().map {})
    }

    override suspend fun hasLike(vacancyId: Int): Boolean {
        return vacanciesDatabase.vacancyDao().hasLike(vacancyId) > 0
    }
}
