package com.valdizz.cocktails.ui.ingredients

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.valdizz.cocktails.model.entity.Ingredient
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource

class IngredientsViewModel(val repository: ICocktailsRepository) : ViewModel() {

    private var searchString = MutableLiveData<String>()

    val ingredients: LiveData<Resource<List<Ingredient>>> = Transformations.switchMap(searchString) { name ->
        if (name.isNullOrEmpty()) {
            repository.getIngredients()
        } else {
            repository.searchIngredientByName(name)
        }
    }

    fun searchIngredient(name: String?) {
        searchString.value = name
    }
}