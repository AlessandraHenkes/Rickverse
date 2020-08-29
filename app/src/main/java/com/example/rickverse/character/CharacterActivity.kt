package com.example.rickverse.character

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.rickverse.R
import com.example.rickverse.favorite.FavoritesSharedPreferencesService
import com.example.rickverse.model.CharacterResponse
import com.example.rickverse.service.RetrofitClient
import com.example.rickverse.util.extension.showToast
import com.example.rickverse.util.viewmodelfactory.CharacterViewModelFactory
import kotlinx.android.synthetic.main.activity_character.*
import kotlinx.android.synthetic.main.toolbar.*

const val CHARACTER_ID = "characterId"

class CharacterActivity : AppCompatActivity() {

    private lateinit var characterViewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        characterViewModel = ViewModelProvider(
            this,
            CharacterViewModelFactory(
                RetrofitClient.getCharacterService()
            )
        ).get(CharacterViewModel::class.java)

        setUI()
        initiateObserves()
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
        fabFavoriteCharacter.setOnClickListener { characterViewModel.toggleFavorite() }
    }

    private fun initiateObserves() {
        characterViewModel.run {
            observeToShowToast(showMessageError, R.string.something_went_wrong)
            observeToShowToast(showMessageLoading, R.string.loading)

            shouldRemoveFromFavorites.observe(this@CharacterActivity, Observer {
                setIsFavoriteCharacter(
                    FavoritesSharedPreferencesService.remove(
                        this@CharacterActivity,
                        id
                    ).not()
                )
                toggleFavoriteIcon()
            })
            shouldAddToFavorites.observe(this@CharacterActivity, Observer {
                setIsFavoriteCharacter(
                    FavoritesSharedPreferencesService.add(
                        this@CharacterActivity,
                        id
                    )
                )
                toggleFavoriteIcon()
            })

            characterInfo.observe(this@CharacterActivity, Observer { character ->
                setCharacter(character)
            })
        }
    }

    private fun loadCharacter() {
        characterViewModel.run {
            intent?.run {
                setId(getIntExtra(CHARACTER_ID, 0))
            }
            setIsFavoriteCharacter(
                FavoritesSharedPreferencesService.isFavorite(
                    this@CharacterActivity,
                    id
                )
            )
        }
        toggleFavoriteIcon()

        characterViewModel.loadCharacter()
    }

    private fun toggleFavoriteIcon() {
        fabFavoriteCharacter.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (characterViewModel.isFavorite.value == true) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
            )
        )
    }

    private fun setCharacter(character: CharacterResponse) {
        with(character) {
            ttCharacter.text = name
            ivCharacter.contentDescription = name
            Glide.with(this@CharacterActivity)
                .load(image)
                .into(ivCharacter)
            tvCharacterStatusValue.text = status.value
            tvCharacterSpecieValue.text = species
            tvCharacterGenderValue.text = gender.value
            tvCharacterOriginValue.text = origin.name
        }
    }

    private fun observeToShowToast(liveData: LiveData<Boolean>, messageId: Int) {
        liveData.observe(this, Observer { shouldShow ->
            if (shouldShow) showToast(messageId)
        })
    }

}