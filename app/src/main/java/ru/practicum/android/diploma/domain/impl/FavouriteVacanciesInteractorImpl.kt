package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.domain.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.FavouriteVacanciesRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetails

class FavouriteVacanciesInteractorImpl(
    private val favouriteVacanciesRepository: FavouriteVacanciesRepository
) : FavouriteVacanciesInteractor {
    override suspend fun getVacancy(vacancyId: String): VacancyDetails {
        return favouriteVacanciesRepository.getVacancy(vacancyId)
    }

    override suspend fun addVacancy(vacancy: VacancyDetails) {
        favouriteVacanciesRepository.addVacancy(vacancy)
    }

    override suspend fun deleteVacancy(vacancyId: String) {
        favouriteVacanciesRepository.deleteVacancy(vacancyId)
    }

    override suspend fun hasLike(vacancyId: String): Boolean {
        return favouriteVacanciesRepository.hasLike(vacancyId)
    }

    override fun listVacancies(): Flow<List<Vacancy>> {
        return favouriteVacanciesRepository.listVacancies().map { list ->
            list.reversed()
        }
    }
}
