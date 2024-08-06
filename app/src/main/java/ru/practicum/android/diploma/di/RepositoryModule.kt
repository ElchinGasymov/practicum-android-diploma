package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.SelectedVacanciesRepositoryImpl


val repositoryModule = module {
    single<SelectedVacanciesRepositoryImpl> {
        SelectedVacanciesRepositoryImpl(get())
    }

}
