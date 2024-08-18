package ru.practicum.android.diploma.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.converters.ConverterIntoEntity
import ru.practicum.android.diploma.data.db.converters.ConverterIntoModel
import ru.practicum.android.diploma.data.interceptors.HeaderInterceptor
import ru.practicum.android.diploma.data.interceptors.LoggingInterceptor
import ru.practicum.android.diploma.data.network.HHApiService
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.sharedPrefs.SharedPrefsStorageFilters
import ru.practicum.android.diploma.data.sharedPrefs.SharedPrefsStorageFiltersImpl

const val HISTORY_MAIN = "historyMain"
const val BASE_URL = "https://api.hh.ru/"
val dataModule = module {
    factory<NetworkClient> {
        RetrofitNetworkClient(
            hhApiService = get(),
            context = androidContext()
        )
    }

    single<HHApiService> {
        val client = OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor)
            .addInterceptor(HeaderInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HHApiService::class.java)
    }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<ConverterIntoModel> {
        ConverterIntoModel()
    }

    single<ConverterIntoEntity> {
        ConverterIntoEntity()
    }

    single<SharedPrefsStorageFilters> {
        SharedPrefsStorageFiltersImpl(
            gson = provideGson(),
            sharedPreferences = get()
        )
    }

    single {
        androidContext().getSharedPreferences(
            HISTORY_MAIN,
            Context.MODE_PRIVATE
        )
    }

}

private fun provideGson() = Gson()
