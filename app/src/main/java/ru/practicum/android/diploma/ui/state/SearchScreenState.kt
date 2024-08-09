package ru.practicum.android.diploma.ui.state

import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.ResponseData.ResponseError

sealed interface SearchScreenState {

    data object Loading : SearchScreenState

    data class LoadNextPage(
        val vacancies: List<Vacancy>
    ) : SearchScreenState

    data object Default : SearchScreenState

    data object NothingFound : SearchScreenState

    data class Success(
        val vacancies: List<Vacancy>,
        val found: Int
    ) : SearchScreenState

    data class Error(
        val error: ResponseError
    ) : SearchScreenState

    data object LoadingNextPage : SearchScreenState
    data class ErrorNextPage(
        val error: ResponseError
    ) : SearchScreenState
}
