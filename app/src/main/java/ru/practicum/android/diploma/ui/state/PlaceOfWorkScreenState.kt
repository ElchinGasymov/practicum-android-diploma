package ru.practicum.android.diploma.ui.state

sealed interface PlaceOfWorkScreenState {
    data class CountryName(
        val countryName: String,
        val countryId: String
    ) : PlaceOfWorkScreenState

    data object NoCountryName : PlaceOfWorkScreenState

    data class RegionName(
        val regionName: String
    ) : PlaceOfWorkScreenState

    data object NoRegionName : PlaceOfWorkScreenState
    data class Saved(
        val countryName: String,
        val regionId: String
    ) : PlaceOfWorkScreenState
}
