package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.CountryInteractor
import ru.practicum.android.diploma.domain.api.FilterRepository
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.util.ResponseData

class CountryInteractorImpl(private val repository: FilterRepository) : CountryInteractor {
    override suspend fun getCountries(): Flow<ResponseData<List<Country>>> {
        return repository.getCountries()
    }
}
