package com.valdizz.cocktails.model.repository

import androidx.lifecycle.LiveData
import com.valdizz.cocktails.model.entity.*

interface ICocktailsRepository {

    fun searchCocktailByName(name: String): LiveData<Resource<List<Cocktail>>>

    fun searchCocktailById(id: Int): LiveData<Resource<Cocktail>>

    fun searchCocktailByIngredient(ingredient: String): LiveData<Resource<List<Cocktail>>>

    fun filterCocktailByType(type: String): LiveData<Resource<List<Cocktail>>>

    fun filterCocktailByCategory(category: String): LiveData<Resource<List<Cocktail>>>

    fun filterCocktailByGlass(glass: String): LiveData<Resource<List<Cocktail>>>

    fun searchIngredientByName(name: String): LiveData<Resource<List<Ingredient>>>

    fun getIngredients(): LiveData<Resource<List<Ingredient>>>

    fun getCategories(): LiveData<Resource<List<Category>>>

    fun getGlasses(): LiveData<Resource<List<Glass>>>

    fun getAlcoholicFilters(): LiveData<Resource<List<Type>>>

    fun randomCocktail(): LiveData<Resource<Cocktail>>

    fun getFavoriteCocktails(): LiveData<Resource<List<Cocktail>>>

    fun setFavoriteCocktail(cocktailId: Int)
}