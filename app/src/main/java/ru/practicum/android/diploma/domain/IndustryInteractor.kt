package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.util.ResponseData

interface IndustryInteractor {
    suspend fun getIndustries(): Flow<ResponseData<List<Industries>>>
}
