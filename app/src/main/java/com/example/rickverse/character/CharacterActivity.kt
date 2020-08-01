package com.example.rickverse.character

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rickverse.R
import com.example.rickverse.favorite.FavoritesSharedPreferencesService
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.toolbar.*

const val CHARACTER_ID = "characterId"

class CharacterActivity : AppCompatActivity() {

    private var id: Int = 0
    private var isFavoriteCharacter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        intent?.run {
            id = getIntExtra(CHARACTER_ID, 0)
        }
        setUI()
        loadCharacter()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        fabFavoriteCharacter.setOnClickListener { toggleFavorite() }
    }

    private fun loadCharacter() {
        isFavoriteCharacter = FavoritesSharedPreferencesService.isFavorite(this, id)
        toggleFavoriteIcon()
    }

    private fun toggleFavorite() {
        isFavoriteCharacter = if (isFavoriteCharacter) {
            FavoritesSharedPreferencesService.remove(this, id).not()
        } else {
            FavoritesSharedPreferencesService.add(this, id)
        }
        toggleFavoriteIcon()
    }

    private fun toggleFavoriteIcon() {
        fabFavoriteCharacter.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (isFavoriteCharacter) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )
        )
    }

}