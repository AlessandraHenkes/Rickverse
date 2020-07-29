package com.example.rickverse.character

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.rickverse.R
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.toolbar.*

const val CHARACTER_ID = "characterId"

class CharacterActivity : AppCompatActivity() {

    private var id: Int = 0
    private var isFavoriteCharacter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
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
    }

    private fun loadCharacter() {
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