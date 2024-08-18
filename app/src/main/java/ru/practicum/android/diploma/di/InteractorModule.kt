package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.DetailsInteractor
import ru.practicum.android.diploma.domain.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.SharedPrefsInteractor
import ru.practicum.android.diploma.domain.FilterInteractor
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.impl.DetailsInteractorImpl
import ru.practicum.android.diploma.domain.impl.FavouriteVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.impl.SharedPrefsInteractorImpl

val interactorModule = module {
    factory<FavouriteVacanciesInteractor> {
        FavouriteVacanciesInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }

    factory<DetailsInteractor> {
        DetailsInteractorImpl(repository = get())
    }

    factory<SharedPrefsInteractor> {
        SharedPrefsInteractorImpl(get())
    }

    factory<FilterInteractor> {
        FilterInteractorImpl(repository = get())
    }
}
