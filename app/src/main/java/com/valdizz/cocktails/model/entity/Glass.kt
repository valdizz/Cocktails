package com.valdizz.cocktails.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Glass(
    @PrimaryKey
    @SerializedName ("strGlass") val glass: String
)