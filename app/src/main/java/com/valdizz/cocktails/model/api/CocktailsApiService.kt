package com.valdizz.cocktails.model.api

import androidx.lifecycle.LiveData
import com.valdizz.cocktails.model.entity.*
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsApiService {

    @GET("search.php")
    fun searchCocktailByName(@Query("s") name: String): LiveData<ApiResponse<Cocktails>>

    @GET("lookup.php")
    fun searchCocktailById(@Query("i") id: Int): LiveData<ApiResponse<Cocktails>>

    @GET("filter.php")
    fun searchCocktailByIngredient(@Query("i") ingredient: String): LiveData<ApiResponse<Cocktails>>

    @GET("filter.php")
    fun filterCocktailByType(@Query("a") type: String): LiveData<ApiResponse<Cocktails>>

    @GET("filter.php")
    fun filterCocktailByCategory(@Query("c") category: String): LiveData<ApiResponse<Cocktails>>

    @GET("filter.php")
    fun filterCocktailByGlass(@Query("g") glass: String): LiveData<ApiResponse<Cocktails>>

    @GET("search.php")
    fun searchIngredientByName(@Query("i") name: String): LiveData<ApiResponse<Ingredients>>

    @GET("list.php?i=list")
    fun getIngredients(): LiveData<ApiResponse<Ingredients>>

    @GET("list.php?c=list")
    fun getCategories(): LiveData<ApiResponse<Categories>>

    @GET("list.php?g=list")
    fun getGlasses(): LiveData<ApiResponse<Glasses>>

    @GET("list.php?a=list")
    fun getTypes(): LiveData<ApiResponse<Types>>

    @GET("random.php")
    fun getRandomCocktail(): LiveData<ApiResponse<Cocktails>>
}