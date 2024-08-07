package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.SelectedVacanciesRepositoryImpl
import ru.practicum.android.diploma.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.domain.api.SearchRepository

val repositoryModule = module {
    single<SelectedVacanciesRepositoryImpl> {
        SelectedVacanciesRepositoryImpl(get(),get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get())
    }
}
