package com.valdizz.cocktails.model.entity

import com.google.gson.annotations.SerializedName

data class Glasses(
    @SerializedName("drinks") val glasses: List<Glass>
)