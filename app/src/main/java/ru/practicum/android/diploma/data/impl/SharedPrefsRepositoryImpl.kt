package ru.practicum.android.diploma.data.impl

import ru.practicum.android.diploma.data.dto.SaveFiltersSharedPrefsDto
import ru.practicum.android.diploma.data.dto.components.CountryDto
import ru.practicum.android.diploma.data.dto.components.IndustriesDto
import ru.practicum.android.diploma.data.dto.components.RegionDto
import ru.practicum.android.diploma.data.dto.toModel
import ru.practicum.android.diploma.data.sharedprefs.SharedPrefsStorageFilters
import ru.practicum.android.diploma.domain.SharedPrefsRepository
import ru.practicum.android.diploma.domain.models.Industries
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs

class SharedPrefsRepositoryImpl(
    private val sharedPrefs: SharedPrefsStorageFilters
) : SharedPrefsRepository {
    override suspend fun read() =
        sharedPrefs.read()?.let { filters ->
            SaveFiltersSharedPrefs(
                industries = filters.industries?.let {
                    Industries(
                        id = it.id,
                        name = filters.industries.name,
                        isChecked = false
                    )
                },
                country = filters.country?.toModel(),
                region = filters.region?.toModel(),
                currency = filters.currency,
                noCurrency = filters.noCurrency
            )
        }

    override suspend fun write(filters: SaveFiltersSharedPrefs) {
        sharedPrefs.write(
            SaveFiltersSharedPrefsDto(
                industries = filters.industries?.let {
                    IndustriesDto(
                        id = it.id,
                        name = filters.industries.name
                    )
                },
                country = filters.country?.let {
                    CountryDto(
                        id = it.id,
                        name = filters.country.name,
                        areas = emptyList(),
                    )
                },
                region = filters.region?.let {
                    RegionDto(
                        parentId = filters.region.parentId,
                        id = filters.region.id,
                        name = it.name
                    )
                },
                currency = filters.currency,
                noCurrency = filters.noCurrency
            )
        )
    }

    override suspend fun clear() {
        sharedPrefs.clear()
    }

}
