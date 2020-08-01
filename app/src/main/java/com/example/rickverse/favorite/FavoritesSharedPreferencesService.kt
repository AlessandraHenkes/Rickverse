package com.example.rickverse.favorite

import android.content.Context
import com.example.rickverse.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val FAVORITES_CHARACTERS = "favoritesCharacters"

object FavoritesSharedPreferencesService {

    private val gson by lazy {
        Gson()
    }

    private val favoritesCharactersType = object : TypeToken<ArrayList<Int>>() {}.type

    private val sharedPreferences = { context: Context? ->
        context?.let { ctx ->
            ctx.getSharedPreferences(
                ctx.getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE
            )
        }
    }

    fun getAll(context: Context?): ArrayList<Int> {
        return sharedPreferences(context)?.getString(FAVORITES_CHARACTERS, "")?.run {
            gson.fromJson<ArrayList<Int>>(this, favoritesCharactersType)
        } ?: arrayListOf()
    }

    fun isFavorite(context: Context?, id: Int) = getAll(context).contains(id)

    fun add(context: Context?, id: Int) =
        writeToSharedPreferences(
            context,
            getAll(context).apply { add(id) }
        )

    fun remove(context: Context?, id: Int) =
        writeToSharedPreferences(
            context,
            getAll(context).apply { remove(id) }
        )

    private fun writeToSharedPreferences(context: Context?, list: ArrayList<Int>): Boolean {
        return sharedPreferences(context)?.run {
            val charactersListJson = gson.toJson(list)
            edit()?.putString(FAVORITES_CHARACTERS, charactersListJson)?.commit()
        } ?: false
    }

}
