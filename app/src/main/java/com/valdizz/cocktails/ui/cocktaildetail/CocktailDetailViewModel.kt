package com.valdizz.cocktails.ui.cocktaildetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource

class CocktailDetailViewModel(val repository: ICocktailsRepository) : ViewModel() {

    private var cocktailId = MutableLiveData<Int>()
    private var randomCocktailId = MutableLiveData<Int>()

    val cocktail: LiveData<Resource<Cocktail>> = Transformations.switchMap(cocktailId) { id ->
        repository.searchCocktailById(id)
    }

    val randomCocktail: LiveData<Resource<Cocktail>> = Transformations.switchMap(randomCocktailId) { id ->
        repository.randomCocktail()
    }

    fun loadCocktail(id: Int) {
        cocktailId.value = id
    }

    fun loadRandomCocktail(id: Int) {
        randomCocktailId.value = id
    }

    fun setFavoriteCocktail(cocktailId: Int) {
        repository.setFavoriteCocktail(cocktailId)
    }
}