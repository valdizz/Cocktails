package com.valdizz.cocktails.ui.cocktails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.valdizz.cocktails.common.Constants.TYPE_ALCOHOLIC
import com.valdizz.cocktails.common.NetworkConnectionManager
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource
import com.valdizz.cocktails.ui.CocktailsActivity

class CocktailsViewModel(
    private val repository: ICocktailsRepository,
    private val connectionManager: NetworkConnectionManager
) : ViewModel() {

    private var typeAndValue = MutableLiveData<Pair<String, String>>()
    val isConnected = MutableLiveData<Boolean>()

    val cocktails: LiveData<Resource<List<Cocktail>>> = Transformations.switchMap(typeAndValue) { data ->
        val type = data.first
        val value = data.second
        when (type) {
            CocktailsActivity.FILTER_COCKTAILS_TAG -> repository.filterCocktailByType(value)
            CocktailsActivity.SEARCH_COCKTAILS_TAG -> repository.searchCocktailByName(value)
            CocktailsActivity.FAVORITES_TAG -> repository.getFavoriteCocktails()
            CocktailsActivity.COCKTAILS_BY_INGREDIENTS_TAG -> repository.searchCocktailByIngredient(value)
            else -> repository.filterCocktailByType(TYPE_ALCOHOLIC)
        }
    }

    fun loadCocktails(typeValue: Pair<String, String>) {
        checkConnection()
        typeAndValue.value = typeValue
    }

    private fun checkConnection() {
        isConnected.value = connectionManager.isConnected
    }
}