package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.SharedPrefsRepository
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs
import ru.practicum.android.diploma.util.ResponseData

class FilterInteractorImpl(
    private val repository: FilterRepository,
    private val repositorySharePrefs: SharedPrefsRepository
) : FilterInteractor {
    override suspend fun getCountries(): Flow<ResponseData<List<Country>>> {
        return repository.getCountries()
    }

    override suspend fun getRegions(id: String): Flow<ResponseData<List<Region>>> {
        return repository.getRegions(id)
    }

    override suspend fun getAllRegions(): Flow<ResponseData<List<Region>>> {
        return repository.getAllRegions()
    }

    override suspend fun getIndustries(): Flow<ResponseData<List<Industries>>> {
        return repository.getIndustries()
    }

    override suspend fun readSharedPrefs(): SaveFiltersSharedPrefs? {
        return repositorySharePrefs.readSharedPrefs()
    }

    override suspend fun writeSharedPrefs(filters: SaveFiltersSharedPrefs) {
        repositorySharePrefs.writeSharedPrefs(filters)
    }

    override suspend fun clearSharedPrefs() {
        repositorySharePrefs.clearSharedPrefs()
    }
}
