package com.valdizz.cocktails.ui.cocktaildetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.valdizz.cocktails.model.entity.Cocktail
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource

class CocktailDetailViewModel(private val repository: ICocktailsRepository) : ViewModel() {

    private var cocktailId = MutableLiveData<Int>()

    val cocktail: LiveData<Resource<Cocktail>> = Transformations.switchMap(cocktailId) { id ->
        if (id > 0) {
            repository.searchCocktailById(id)
        } else {
            repository.randomCocktail()
        }
    }

    fun loadCocktail(id: Int) {
        cocktailId.value = id
    }

    fun setFavoriteCocktail(cocktailId: Int) {
        repository.setFavoriteCocktail(cocktailId)
    }
}