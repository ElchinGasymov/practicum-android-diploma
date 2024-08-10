package ru.practicum.android.diploma.ui.state

sealed interface FavouriteState {
    data object Favourite : FavouriteState
    data object NotFavourite : FavouriteState
}
