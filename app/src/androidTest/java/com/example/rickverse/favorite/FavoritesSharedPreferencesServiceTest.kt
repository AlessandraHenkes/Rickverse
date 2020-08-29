package com.example.rickverse.favorite

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rickverse.R
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesSharedPreferencesServiceTest {

    private val favoritesCharacterKey = "favoritesCharacters"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>().also { ctx ->
            sharedPreferences = ctx.getSharedPreferences(
                ctx.getString(R.string.shared_preferences_key),
                Context.MODE_PRIVATE
            )

            sharedPreferences.edit().clear().commit()
        }
    }

    @Test
    fun getAllShouldReturnEmptyArrayWhenNothingHasBeenAdded() {
        assertEquals(arrayListOf<Int>(), FavoritesSharedPreferencesService.getAll(context))
    }

    @Test
    fun getAllShouldReturnArrayWhenDataHasBeenAdded() {
        sharedPreferences.edit().putString(favoritesCharacterKey, "[1,2,3,4,5]").commit()
        assertEquals(
            arrayListOf(1, 2, 3, 4, 5),
            FavoritesSharedPreferencesService.getAll(context)
        )
    }

    @Test
    fun isFavoriteShouldReturnFalseWhenIdHasNotBeenAdded() {
        assertFalse(FavoritesSharedPreferencesService.isFavorite(context, 1))
    }

    @Test
    fun isFavoriteShouldReturnTrueWhenIdHasBeenAdded() {
        sharedPreferences.edit().putString(favoritesCharacterKey, "[1]").commit()
        assertTrue(FavoritesSharedPreferencesService.isFavorite(context, 1))
    }

    @Test
    fun addShouldAddId() {
        FavoritesSharedPreferencesService.add(context, 1)
        assertEquals("[1]", sharedPreferences.getString(favoritesCharacterKey, ""))
    }

    @Test
    fun addShouldAddIdToCurrentString() {
        sharedPreferences.edit().putString(favoritesCharacterKey, "[1]").commit()
        FavoritesSharedPreferencesService.add(context, 2)
        assertEquals("[1,2]", sharedPreferences.getString(favoritesCharacterKey, ""))
    }

    @Test
    fun removeShouldRemoveIdIfDataHasBeenAdded() {
        sharedPreferences.edit().putString(favoritesCharacterKey, "[1]").commit()
        FavoritesSharedPreferencesService.remove(context, 1)
        assertEquals("[]", sharedPreferences.getString(favoritesCharacterKey, ""))
    }

    @Test
    fun removeShouldNotRemoveIdIfDataHasNotBeenAdded() {
        sharedPreferences.edit().putString(favoritesCharacterKey, "[1]").commit()
        FavoritesSharedPreferencesService.remove(context, 2)
        assertEquals("[1]", sharedPreferences.getString(favoritesCharacterKey, ""))
    }

}
