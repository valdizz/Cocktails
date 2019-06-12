package com.valdizz.cocktails.model.entity

import com.google.gson.annotations.SerializedName

/**
 * Ingredients data class.
 *
 * @author Vlad Kornev
 */
data class Ingredients(
    @SerializedName(value = "ingredients", alternate = ["drinks"]) val ingredients: List<Ingredient>
)