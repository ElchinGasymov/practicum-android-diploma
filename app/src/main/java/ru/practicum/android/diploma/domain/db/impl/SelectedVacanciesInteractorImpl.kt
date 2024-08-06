package ru.practicum.android.diploma.domain.db.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.db.SelectedVacanciesInteractor

class SelectedVacanciesInteractorImpl(
    private val selectedVacanciesRepository: SelectedVacanciesRepository
) : SelectedVacanciesInteractor {
    override suspend fun getVacancy(vacancyId: Long): Vacancy {
        return selectedVacanciesRepository.getVacancy(vacancyId)
    }

    override suspend fun addVacancy(vacancy: Vacancy) {
        selectedVacanciesRepository.addVacancy(vacancy)
    }

    override suspend fun deleteVacancy(vacancyId: Long) {
        selectedVacanciesRepository.deleteVacancy(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> {
        return selectedVacanciesRepository.listVacancies().map { list ->
            list.reversed()
        }
    }
}
