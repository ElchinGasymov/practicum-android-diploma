package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.api.SearchRepository
import ru.practicum.android.diploma.domain.models.VacanciesResponse
import ru.practicum.android.diploma.util.Options
import ru.practicum.android.diploma.util.ResponseData

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    override fun search(options: Options): Flow<ResponseData<VacanciesResponse>> {
        return repository.search(options)
    }
}
