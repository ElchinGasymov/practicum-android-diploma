package ru.practicum.android.diploma.ui.state

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs

sealed interface PlaceOfWorkScreenState {
    data class CountryName(
        val country: Country
    ) : PlaceOfWorkScreenState

    data object NoCountryName : PlaceOfWorkScreenState

    data class RegionName(
        val region: Region
    ) : PlaceOfWorkScreenState

    data object NoRegionName : PlaceOfWorkScreenState
    data class Saved(
        val country: Country,
        val region: Region
    ) : PlaceOfWorkScreenState
    data class Loaded(
        val filters: SaveFiltersSharedPrefs
    ) : PlaceOfWorkScreenState
}
