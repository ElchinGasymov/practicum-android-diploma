package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.ResponseData

interface RegionInteractor {
    suspend fun getCountries(id: String): Flow<ResponseData<List<Region>>>
}
