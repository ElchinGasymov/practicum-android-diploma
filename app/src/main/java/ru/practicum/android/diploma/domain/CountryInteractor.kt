package ru.practicum.android.diploma.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.util.ResponseData

interface CountryInteractor {
    suspend fun getCountries(): Flow<ResponseData<List<Country>>>
}
