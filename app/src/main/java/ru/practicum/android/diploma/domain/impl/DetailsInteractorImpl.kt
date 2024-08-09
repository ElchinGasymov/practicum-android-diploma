package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.DetailsInteractor
import ru.practicum.android.diploma.domain.api.VacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.ResponseData

class DetailsInteractorImpl(private val repository: VacancyRepository) : DetailsInteractor {
    override fun getVacancy(id: String): Flow<ResponseData<VacancyDetails>> {
        return repository.getVacancy(id)
    }
}
