package ru.practicum.android.diploma.ui.state

import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs

sealed interface FilterScreenState {
    data class PlaceOfWork(
        val countryName: String
    ) : FilterScreenState

    data object ClearState : FilterScreenState
    data class Industry(
        val industry: String
    ) : FilterScreenState

    data object NoPlaceOfWork : FilterScreenState
    data object NoIndustry : FilterScreenState
    data class FiltersSaved(
        val filters: SaveFiltersSharedPrefs
    ) : FilterScreenState

    data class FiltersLoaded(
        val filters: SaveFiltersSharedPrefs
    ) : FilterScreenState
}
