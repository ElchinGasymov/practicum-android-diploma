package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.SearchInteractor
import ru.practicum.android.diploma.domain.db.FavouriteVacanciesInteractor
import ru.practicum.android.diploma.domain.db.impl.FavouriteVacanciesInteractorImpl
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl

val interactorModule = module {
    factory<FavouriteVacanciesInteractor> {
        FavouriteVacanciesInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
}
