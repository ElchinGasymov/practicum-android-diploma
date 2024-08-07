package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.db.SelectedVacanciesInteractor
import ru.practicum.android.diploma.domain.db.impl.SelectedVacanciesInteractorImpl

val interactorModule = module {
    factory<SelectedVacanciesInteractor> {
        SelectedVacanciesInteractorImpl(get())
    }

    factory<SearchInteractor> {
        SearchInteractorImpl(repository = get())
    }
}
