package com.valdizz.cocktails.ui.cocktails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.valdizz.cocktails.common.NetworkConnectionManager
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource

class CocktailsViewModel(
    private val repository: ICocktailsRepository,
    private val connectionManager: NetworkConnectionManager
) : ViewModel() {

    private var searchString = MutableLiveData<String>()
    private var ingredientName = MutableLiveData<String>()
    val isConnected = MutableLiveData<Boolean>()

    val cocktails: LiveData<Resource<List<Cocktail>>> = Transformations.switchMap(searchString) { name ->
        repository.searchCocktailByName(name)
    }

    val favoriteCocktails: LiveData<Resource<List<Cocktail>>>
        get() {
            return repository.getFavoriteCocktails()
        }

    val cocktailsByIngredient: LiveData<Resource<List<Cocktail>>> = Transformations.switchMap(ingredientName) { name ->
        repository.searchCocktailByIngredient(name)
    }

    fun searchCocktails(name: String) {
        checkConnection()
        searchString.value = name
    }

    fun searchCocktailsByIngredient(name: String) {
        checkConnection()
        ingredientName.value = name
    }

    private fun checkConnection() {
        isConnected.value = connectionManager.isConnected
    }
}