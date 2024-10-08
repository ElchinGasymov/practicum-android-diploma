package ru.practicum.android.diploma.presentation.viewmodels.favourites

import ru.practicum.android.diploma.domain.models.Vacancy

data class FavouritesScreenState(
    val state: FavouritesDbState,
    val list: List<Vacancy>
)
