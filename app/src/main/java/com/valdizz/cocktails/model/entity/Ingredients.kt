package com.valdizz.cocktails.model.entity

import com.google.gson.annotations.SerializedName

data class Ingredients(
    @SerializedName(value = "ingredients", alternate = ["drinks"]) val ingredients: List<Ingredient>
)