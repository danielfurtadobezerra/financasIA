package com.ia.financias

import android.app.Application
import com.ia.financias.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FinanciasApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@FinanciasApplication)
            modules(appModule)
        }
    }
}
