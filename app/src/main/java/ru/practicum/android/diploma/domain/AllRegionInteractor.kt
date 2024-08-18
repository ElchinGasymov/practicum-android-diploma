package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.ResponseData

interface AllRegionInteractor {
    suspend fun getAllRegions(): Flow<ResponseData<List<Region>>>
}
