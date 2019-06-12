package com.valdizz.cocktails.model.repository

import androidx.lifecycle.LiveData
import com.valdizz.cocktails.model.entity.*

/**
 * Repository interface.
 *
 * @author Vlad Kornev
 */
interface ICocktailsRepository {

    fun searchCocktailByName(name: String): LiveData<Resource<List<Cocktail>>>

    fun searchCocktailById(id: Int): LiveData<Resource<Cocktail>>

    fun searchCocktailByIngredient(ingredient: String): LiveData<Resource<List<Cocktail>>>

    fun filterCocktailByType(type: String): LiveData<Resource<List<Cocktail>>>

    fun searchIngredientByName(name: String): LiveData<Resource<List<Ingredient>>>

    fun getIngredients(): LiveData<Resource<List<Ingredient>>>

    fun randomCocktail(): LiveData<Resource<Cocktail>>

    fun getFavoriteCocktails(): LiveData<Resource<List<Cocktail>>>

    fun setFavoriteCocktail(cocktailId: Int)
}