package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.IndustryInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.util.ResponseData

class IndustryInteractorImpl(private val repository: FilterRepository) : IndustryInteractor {
    override suspend fun getIndustries(): Flow<ResponseData<List<Industries>>> {
        return repository.getIndustries()
    }
}
