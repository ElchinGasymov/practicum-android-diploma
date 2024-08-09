package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.ResponseData

interface DetailsInteractor {
    fun getVacancy(id: String): Flow<ResponseData<VacancyDetails>>
}
