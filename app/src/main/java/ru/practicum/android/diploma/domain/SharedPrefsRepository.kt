package ru.practicum.android.diploma.domain

import ru.practicum.android.diploma.domain.models.SaveFiltersSharedPrefs

interface SharedPrefsRepository {
    suspend fun readSharedPrefs(): SaveFiltersSharedPrefs?
    suspend fun writeSharedPrefs(filters: SaveFiltersSharedPrefs)
    suspend fun clearSharedPrefs()
}
