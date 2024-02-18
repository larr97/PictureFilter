package com.example.picturefilter.utilities

import android.app.Application
import com.example.picturefilter.dependencyinjection.repositoryModule
import com.example.picturefilter.dependencyinjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core. context.startKoin

@Suppress()
class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}