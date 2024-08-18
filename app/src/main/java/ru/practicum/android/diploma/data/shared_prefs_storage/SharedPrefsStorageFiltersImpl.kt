package ru.practicum.android.diploma.data.shared_prefs_storage

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.data.dto.SaveFiltersSharedPrefsDto

class SharedPrefsStorageFiltersImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : SharedPrefsStorageFilters {
    override suspend fun read(): SaveFiltersSharedPrefsDto? {
        return gson.fromJson(sharedPreferences.getString(HISTORY, null), SaveFiltersSharedPrefsDto::class.java)
    }

    override suspend fun write(filters: SaveFiltersSharedPrefsDto) {
        sharedPreferences.edit().putString(HISTORY, gson.toJson(filters)).apply()
    }

    override suspend fun clear() {
        sharedPreferences.edit().remove(HISTORY).apply()
    }

    companion object {
        private const val HISTORY = "history"
    }
}
