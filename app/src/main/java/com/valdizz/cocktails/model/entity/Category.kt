package com.valdizz.cocktails.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Category(
    @PrimaryKey
    @SerializedName("strCategory") val category: String
)