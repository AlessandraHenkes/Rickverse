package com.example.rickverse.model

import com.google.gson.annotations.SerializedName

enum class Status(val value: String) {
    @SerializedName("unknown")
    UNKNOWN("Unknown"),

    @SerializedName("Alive")
    ALIVE("Alive"),

    @SerializedName("Dead")
    DEAD("Dead");
}
