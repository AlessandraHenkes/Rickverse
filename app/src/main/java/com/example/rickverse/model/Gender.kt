package com.example.rickverse.model

import com.google.gson.annotations.SerializedName

enum class Gender(val value: String) {
    @SerializedName("Female")
    FEMALE("Female"),

    @SerializedName("Male")
    MALE("Male"),

    @SerializedName("Genderless")
    GENDERLESS("Genderless"),

    @SerializedName("unknown")
    UNKNOWN("Unknown")
}