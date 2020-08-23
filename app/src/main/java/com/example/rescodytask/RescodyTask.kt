package com.example.rescodytask

import android.app.Application
import android.content.Context
import com.example.rescodytask.di.networkModule
import com.example.rescodytask.di.repositoriesModule
import com.example.rescodytask.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RescodyTask : Application() {
    companion object {
        lateinit var context: Context
        fun getAppContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize the SDK

        // Initialize the SDK
        context = applicationContext
        startKoin {
            androidContext(this@RescodyTask)
            androidLogger()
            modules(listOf(viewModelModule, networkModule, repositoriesModule))
        }
    }
}