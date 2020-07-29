package com.example.rickverse.model

import com.google.gson.annotations.SerializedName

data class CharactersResponse(
    val info: Info,
    @SerializedName("results")
    val characters: List<CharacterPreview>
)