package ru.practicum.android.diploma.domain.db.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.db.SelectedVacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class SelectedVacanciesInteractorImpl(
    private val selectedVacanciesRepository: SelectedVacanciesRepository
) : SelectedVacanciesInteractor {
    override suspend fun getVacancy(vacancyId: Int): VacancyDetails {
        return selectedVacanciesRepository.getVacancy(vacancyId)
    }

    override suspend fun addVacancy(vacancy: VacancyDetails) {
        selectedVacanciesRepository.addVacancy(vacancy)
    }

    override suspend fun deleteVacancy(vacancyId: Int) {
        selectedVacanciesRepository.deleteVacancy(vacancyId)
    }

    override suspend fun hasLike(vacancyId: Int): Boolean {
        return selectedVacanciesRepository.hasLike(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> {
        return selectedVacanciesRepository.listVacancies().map { list ->
            list.reversed()
        }
    }
}
