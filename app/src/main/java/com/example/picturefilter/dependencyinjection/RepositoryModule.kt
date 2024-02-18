package com.example.picturefilter.dependencyinjection

import com.example.picturefilter.repositories.EditImageRepository
import com.example.picturefilter.repositories.EditImageRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
}