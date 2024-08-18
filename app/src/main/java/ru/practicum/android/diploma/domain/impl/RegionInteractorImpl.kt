package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.RegionInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.ResponseData

class RegionInteractorImpl(private val repository: FilterRepository) : RegionInteractor {
    override suspend fun getCountries(id: String): Flow<ResponseData<List<Region>>> {
        return repository.getRegions(id)
    }
}
