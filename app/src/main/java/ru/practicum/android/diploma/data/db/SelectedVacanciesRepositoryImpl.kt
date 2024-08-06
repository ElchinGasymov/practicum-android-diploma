package ru.practicum.android.diploma.data.db

import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.domain.db.SelectedVacanciesRepository
import java.util.concurrent.Flow

class SelectedVacanciesRepositoryImpl(
    private val vacanciesDatabase: AppDatabase
) : SelectedVacanciesRepository {
    override suspend fun getVacancy(vacancyId: Long): Vacancy {
        return vacanciesDatabase.vacancyDao.findVacancy(vacancyId).map {}
    }

    override suspend fun addVacancy(vacancy: Vacancy) {
        vacanciesDatabase.vacancyDao.insertVacancy(vacancy.map {})
    }

    override suspend fun deleteVacancy(vacancyId: Long) {
        vacanciesDatabase.vacancyDao().deleteVacancy(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> = flow {
        emit(vacanciesDatabase.vacancyDao().getSelectedVacancies().map {})
    }
}
