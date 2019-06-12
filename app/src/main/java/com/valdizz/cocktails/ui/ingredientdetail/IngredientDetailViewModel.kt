package com.valdizz.cocktails.ui.ingredientdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.valdizz.cocktails.model.entity.Ingredient
import com.valdizz.cocktails.model.repository.ICocktailsRepository
import com.valdizz.cocktails.model.repository.Resource

/**
 * ViewModel class implements the logic to manage ingredient details.
 *
 * @author Vlad Kornev
 */
class IngredientDetailViewModel(private val repository: ICocktailsRepository) : ViewModel() {

    private var ingredientName = MutableLiveData<String>()

    val ingredient: LiveData<Resource<List<Ingredient>>> = Transformations.switchMap(ingredientName) { name ->
        repository.searchIngredientByName(name)
    }

    fun loadIngredient(name: String) {
        ingredientName.value = name
    }
}