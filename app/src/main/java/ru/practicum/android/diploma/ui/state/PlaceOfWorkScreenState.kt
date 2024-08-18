package ru.practicum.android.diploma.ui.state

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.domain.models.Region

sealed interface PlaceOfWorkScreenState {
    data class CountryName(
        val country: Country
    ) : PlaceOfWorkScreenState

    data object NoCountryName : PlaceOfWorkScreenState

    data class RegionName(
        val regionName: String
    ) : PlaceOfWorkScreenState

    data object NoRegionName : PlaceOfWorkScreenState
    data class Saved(
        val country: Country,
        val region: Region
    ) : PlaceOfWorkScreenState
}
