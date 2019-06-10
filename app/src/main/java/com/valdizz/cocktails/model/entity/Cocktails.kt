package com.valdizz.cocktails.model.entity

import com.google.gson.annotations.SerializedName

data class Cocktails(
    @SerializedName("drinks") val drinks: List<Cocktail>
)