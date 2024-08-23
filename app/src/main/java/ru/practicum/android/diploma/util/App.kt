package ru.practicum.android.diploma.util

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.di.viewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule,
                interactorModule,
                repositoryModule,
                dataModule
            )
        }
    }
    companion object {
        const val REGION_ID_KEY = "REGION_ID_KEY"
        const val PLACE_OF_WORK_KEY = "PLACE_OF_WORK_KEY"
        const val PLACE_OF_WORK_COUNTRY_KEY = "PLACE_OF_WORK_COUNTRY_KEY"
        const val PLACE_OF_WORK_REGION_KEY = "PLACE_OF_WORK_REGION_KEY"
    }
}
