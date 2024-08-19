package ru.practicum.android.diploma.ui.state

import ru.practicum.android.diploma.domain.models.Region
import ru.practicum.android.diploma.util.ResponseData.ResponseError

sealed interface RegionsScreenState {
    data object Default : RegionsScreenState
    data object Loading : RegionsScreenState
    data class Error(
        val error: ResponseError
    ) : RegionsScreenState

    data class Success(
        val regions: List<Region>
    ) : RegionsScreenState
}
