package com.example.gymknight

import android.app.Application
import com.example.gymknight.di.appModule
import com.example.gymknight.di.viewModelModule
import org.koin.core.context.startKoin

class MyApplication:  Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            modules(
                appModule,
                viewModelModule,
            )
        }
    }
}