package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs

interface SharedPrefsInteractor {
    suspend fun read(): SaveFiltersSharedPrefs?

    suspend fun write(filters: SaveFiltersSharedPrefs)

    suspend fun clear()
}
