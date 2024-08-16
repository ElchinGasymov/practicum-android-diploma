package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.Sector
import ru.practicum.android.diploma.util.ResponseData

interface FilterRepository {
    fun getCountries(): Flow<ResponseData<List<Country>>>
    fun getRegions(id: Int): Flow<ResponseData<List<Region>>>
    fun getAllRegions(): Flow<ResponseData<List<Region>>>
    fun getSectors(): Flow<ResponseData<List<Sector>>>
}
