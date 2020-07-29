package com.example.rickverse.service

import com.example.rickverse.model.CharacterResponse
import com.example.rickverse.model.CharactersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("character")
    fun getAll(@Query("page") page: Int? = null): Call<CharactersResponse>

    @GET("character/{id}")
    fun getCharacterInfo(@Path("id") id: Int): Call<CharacterResponse>

}