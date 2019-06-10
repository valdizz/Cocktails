package com.valdizz.cocktails

import android.app.Application
import com.valdizz.cocktails.di.cocktailsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CocktailsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CocktailsApplication)
            modules(cocktailsModule)
        }
    }
}