package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.viewmodels.FilterIndustryViewModel
import ru.practicum.android.diploma.presentation.viewmodels.SearchViewModel
import ru.practicum.android.diploma.presentation.viewmodels.VacancyViewModel
import ru.practicum.android.diploma.presentation.viewmodels.favourites.FavouritesFragmentViewModel

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

    viewModel<FilterIndustryViewModel>{
        FilterIndustryViewModel(get())
    }
}
