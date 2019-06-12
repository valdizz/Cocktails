package com.valdizz.cocktails.di

import androidx.room.Room
import com.valdizz.cocktails.common.AppExecutors
import com.valdizz.cocktails.common.Constants
import com.valdizz.cocktails.ui.network.NetworkConnectionLiveData
import com.valdizz.cocktails.common.RateLimiter
import com.valdizz.cocktails.model.api.CocktailsApiService
import com.valdizz.cocktails.model.api.LiveDataCallAdapterFactory
import com.valdizz.cocktails.model.db.CocktailsDb
import com.valdizz.cocktails.model.repository.CocktailsRepository
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.ui.network.NetworkConnectionViewModel
import com.valdizz.cocktails.ui.cocktaildetail.CocktailDetailViewModel
import com.valdizz.cocktails.ui.cocktails.CocktailsViewModel
import com.valdizz.cocktails.ui.ingredientdetail.IngredientDetailViewModel
import com.valdizz.cocktails.ui.ingredients.IngredientsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Koin module (DI).
 *
 * @author Vlad Kornev
 */
val cocktailsModule = module {

    viewModel { CocktailsViewModel(get()) }
    viewModel { CocktailDetailViewModel(get()) }
    viewModel { IngredientsViewModel(get()) }
    viewModel { IngredientDetailViewModel(get()) }
    viewModel { NetworkConnectionViewModel(androidApplication()) }
    single<ICocktailsRepository> { CocktailsRepository(get(), get(), get(), get()) }
    single { AppExecutors() }
    single { RateLimiter<String>(Constants.UPDATE_PERIOD, TimeUnit.MINUTES) }
    single { NetworkConnectionLiveData(androidApplication()) }
    single {
        Room.databaseBuilder(androidApplication(), CocktailsDb::class.java, "cocktails.db")
        .fallbackToDestructiveMigration()
        .build()
    }
    single { get<CocktailsDb>().cocktailsDao() }
    single { createWebService<CocktailsApiService>(Constants.BASE_URL) }
}

inline fun <reified T> createWebService(url: String): T {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .baseUrl(url)
        .build()
    return retrofit.create(T::class.java)
}
