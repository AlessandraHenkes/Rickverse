package com.example.rickverse.service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_DELAY = 30L
private const val BASE_URL = "https://rickandmortyapi.com/api/"

object RetrofitClient {

    private val gson by lazy { GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create() }
    private var retrofit: Retrofit? = null

    fun getCharacterService(): CharactersService {
        return getOrCreateRetrofit().create(CharactersService::class.java)
    }

    private fun getOrCreateRetrofit(): Retrofit {
        return retrofit ?: run {
            val client = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_DELAY, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_DELAY, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(client)
                .build().apply {
                    retrofit = this
                }
        }
    }

}
