package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.ResponseData

interface FilterInteractor {
    suspend fun getCountries(): Flow<ResponseData<List<Country>>>
    suspend fun getRegions(id: String): Flow<ResponseData<List<Region>>>
    suspend fun getAllRegions(): Flow<ResponseData<List<Region>>>
    suspend fun getIndustries(): Flow<ResponseData<List<Industries>>>
}
