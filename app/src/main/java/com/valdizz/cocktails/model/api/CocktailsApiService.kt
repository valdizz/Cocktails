package com.valdizz.cocktails.model.api

import androidx.lifecycle.LiveData
import com.valdizz.cocktails.model.entity.*
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for getting cocktails/ingredients from TheCocktailDB.com.
 *
 * @author Vlad Kornev
 */
interface CocktailsApiService {

    @GET("search.php")
    fun searchCocktailByName(@Query("s") name: String): LiveData<ApiResponse<Cocktails>>

    @GET("lookup.php")
    fun searchCocktailById(@Query("i") id: Int): LiveData<ApiResponse<Cocktails>>

    @GET("filter.php")
    fun searchCocktailByIngredient(@Query("i") ingredient: String): LiveData<ApiResponse<Cocktails>>

    @GET("filter.php")
    fun filterCocktailByType(@Query("a") type: String): LiveData<ApiResponse<Cocktails>>

    @GET("search.php")
    fun searchIngredientByName(@Query("i") name: String): LiveData<ApiResponse<Ingredients>>

    @GET("list.php?i=list")
    fun getIngredients(): LiveData<ApiResponse<Ingredients>>

    @GET("random.php")
    fun getRandomCocktail(): LiveData<ApiResponse<Cocktails>>
}