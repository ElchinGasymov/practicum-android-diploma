package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.AllRegionInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.ResponseData

class AllRegionInteractorImpl(private val repository: FilterRepository) : AllRegionInteractor {
    override suspend fun getAllRegions(): Flow<ResponseData<List<Region>>> {
        return repository.getAllRegions()
    }
}
