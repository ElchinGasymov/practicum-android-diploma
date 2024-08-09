package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.ResponseData

interface VacancyRepository {
    fun getVacancy(id: Int): Flow<ResponseData<VacancyDetails>>
}
