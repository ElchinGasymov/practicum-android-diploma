package ru.practicum.android.diploma.ui.state

import ru.practicum.android.diploma.domain.models.Country
import ru.practicum.android.diploma.util.ResponseData.ResponseError

sealed interface CountriesScreenState {
    data object Default : CountriesScreenState
    data object Loading : CountriesScreenState
    data class Success(
        val regions: List<Country>
    ) : CountriesScreenState

    data class Error(
        val error: ResponseError
    ) : CountriesScreenState
}
