package com.example.rickverse.character

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rickverse.R
import com.example.rickverse.databinding.ActivityCharacterBinding
import com.example.rickverse.favorite.FavoritesSharedPreferencesService
import com.example.rickverse.service.RetrofitClient
import com.example.rickverse.util.extension.showToast
import com.example.rickverse.util.viewmodelfactory.CharacterViewModelFactory
import kotlinx.android.synthetic.main.toolbar.*

const val CHARACTER_ID = "characterId"

class CharacterActivity : AppCompatActivity() {

    private lateinit var characterViewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterViewModel = ViewModelProvider(
            this,
            CharacterViewModelFactory(
                RetrofitClient.getCharacterService()
            )
        ).get(CharacterViewModel::class.java)
        initiateBinding()
        setUI()
        initiateObserves()
        loadCharacter()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initiateBinding() {
        DataBindingUtil.setContentView<ActivityCharacterBinding>(
            this,
            R.layout.activity_character
        ).apply {
            lifecycleOwner = this@CharacterActivity
            vm = characterViewModel
        }
    }

    private fun setUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
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
            })
            shouldAddToFavorites.observe(this@CharacterActivity, Observer {
                setIsFavoriteCharacter(
                    FavoritesSharedPreferencesService.add(
                        this@CharacterActivity,
                        id
                    )
                )
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

            loadCharacter()
        }
    }

    private fun observeToShowToast(liveData: LiveData<Boolean>, messageId: Int) {
        liveData.observe(this, Observer { shouldShow ->
            if (shouldShow) showToast(messageId)
        })
    }

}