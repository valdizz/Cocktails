package com.valdizz.cocktails.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Type(
    @PrimaryKey
    @SerializedName("strAlcoholic") val type: String
)
