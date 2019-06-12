package com.valdizz.cocktails.model.entity

import com.google.gson.annotations.SerializedName

/**
 * Cocktails data class.
 *
 * @author Vlad Kornev
 */
data class Cocktails(
    @SerializedName("drinks") val drinks: List<Cocktail>
)