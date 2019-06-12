package com.valdizz.cocktails.model.entity

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

/**
 * Ingredient data class.
 *
 * @author Vlad Kornev
 */
@Entity(
    indices = [Index("ingredient")],
    primaryKeys = ["ingredient"]
)
data class Ingredient(
    @SerializedName("idIngredient") val id: Int?,
    @SerializedName(value = "strIngredient", alternate = ["strIngredient1"]) val ingredient: String,
    @SerializedName("strDescription") val description: String?,
    @SerializedName("strType") val type: String?
)