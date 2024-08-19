package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs
import ru.practicum.android.diploma.util.ResponseData

interface FilterRepository {
    fun getCountries(): Flow<ResponseData<List<Country>>>
    fun getRegions(id: String): Flow<ResponseData<List<Region>>>
    fun getAllRegions(): Flow<ResponseData<List<Region>>>
    fun getIndustries(): Flow<ResponseData<List<Industries>>>
}
