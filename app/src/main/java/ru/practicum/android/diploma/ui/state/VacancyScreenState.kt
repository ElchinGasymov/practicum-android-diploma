package ru.practicum.android.diploma.ui.state

import ru.practicum.android.diploma.domain.models.VacancyDetails
import ru.practicum.android.diploma.util.ResponseData.ResponseError

sealed interface VacancyScreenState {
    data object Loading : VacancyScreenState
    data object Default : VacancyScreenState

    data class Success(
        val vacancyDetails: VacancyDetails
    ) : VacancyScreenState

    data class Error(
        val error: ResponseError
    ) : VacancyScreenState
}
