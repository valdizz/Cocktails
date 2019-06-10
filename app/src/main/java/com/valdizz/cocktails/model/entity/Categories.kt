package com.valdizz.cocktails.model.entity

import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("drinks") val categories: List<Category>
)