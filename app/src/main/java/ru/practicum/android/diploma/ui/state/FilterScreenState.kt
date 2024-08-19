package ru.practicum.android.diploma.ui.state

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
}
