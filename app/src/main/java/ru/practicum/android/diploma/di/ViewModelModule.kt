package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favourites.FavouritesFragmentViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel

val viewModelModule = module {
    viewModel<FavouritesFragmentViewModel> {
        FavouritesFragmentViewModel(get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get())
    }

    viewModel<VacancyViewModel> {
        VacancyViewModel(get(), get())
    }
}
