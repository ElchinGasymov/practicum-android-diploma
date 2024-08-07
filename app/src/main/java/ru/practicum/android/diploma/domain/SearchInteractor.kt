package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.util.Options
import ru.practicum.android.diploma.util.ResponseData

interface SearchInteractor {
    fun search(options: Options): Flow<ResponseData<VacanciesResponse>>
}
