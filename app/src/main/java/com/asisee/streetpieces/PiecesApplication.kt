package com.asisee.streetpieces

import android.app.Application
import com.asisee.streetpieces.model.service.module.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class PiecesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PiecesApplication)
            modules(AppModule().module)
        }
    }
}