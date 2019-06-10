package com.valdizz.cocktails.model.entity

import com.google.gson.annotations.SerializedName

data class Types(
    @SerializedName("drinks") val types: List<Type>
)