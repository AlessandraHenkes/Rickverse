package com.example.rickverse.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickverse.model.CharacterPreview
import com.example.rickverse.service.CharactersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesViewModel(
    private val charactersService: CharactersService
) : ViewModel() {

    private var favoritesIds: List<Int> = listOf()

    fun setFavorites(favorites: List<Int>) {
        favoritesIds = favorites
    }

    private val _showMessageError = MutableLiveData<Boolean>()
    val showMessageError: LiveData<Boolean>
        get() = _showMessageError

    private val _showMessageLoading = MutableLiveData<Boolean>()
    val showMessageLoading: LiveData<Boolean>
        get() = _showMessageLoading

    private val _showMessageNoFavorites = MutableLiveData<Boolean>()
    val showMessageNoFavorites: LiveData<Boolean>
        get() = _showMessageNoFavorites

    private val _characters = MutableLiveData<List<CharacterPreview>>()
    val characters: LiveData<List<CharacterPreview>>
        get() = _characters

    fun loadFavorites() {
        if (favoritesIds.isEmpty()) {
            _characters.postValue(listOf())
            _showMessageNoFavorites.postValue(true)
            return
        }
        _showMessageLoading.postValue(true)
        _showMessageNoFavorites.postValue(false)
        charactersService
            .getFavorites(favoritesIds)
            .enqueue(object : Callback<List<CharacterPreview>> {
                override fun onResponse(
                    call: Call<List<CharacterPreview>>?,
                    response: Response<List<CharacterPreview>>?
                ) {
                    _showMessageLoading.postValue(false)
                    response?.run {
                        _showMessageError.postValue(isSuccessful.not())
                        if (isSuccessful) body()?.let { _characters.postValue(it) }
                    }
                }

                override fun onFailure(call: Call<List<CharacterPreview>>?, t: Throwable?) {
                    _showMessageLoading.postValue(false)
                    _showMessageError.postValue(true)
                }
            })
    }

}
