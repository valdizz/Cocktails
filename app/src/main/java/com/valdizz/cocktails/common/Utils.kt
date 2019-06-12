package com.valdizz.cocktails.common

import android.content.res.Resources
import com.valdizz.cocktails.model.entity.Ingredient

/**
 * Extension functions.
 *
 * @author Vlad Kornev
 */
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun List<Ingredient>.ingredientNameCapitalize(): List<Ingredient> {
    val newIngredients = mutableListOf<Ingredient>()
    for (item in this) {
        val newIngredient = item.ingredient.toLowerCase().capitalize()
        newIngredients.add(item.copy(ingredient = newIngredient))
    }
    return newIngredients
}