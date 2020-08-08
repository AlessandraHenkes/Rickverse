package com.example.rickverse.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickverse.model.CharacterResponse
import com.example.rickverse.service.CharactersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharacterViewModel(
    private val charactersService: CharactersService
) : ViewModel() {

    private val _id = MutableLiveData<Int>()
    val id: Int
        get() = _id.value ?: 0

    fun setId(id: Int) {
        _id.value = id
    }

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private val _shouldRemoveFromFavorites = MutableLiveData<Boolean>()
    val shouldRemoveFromFavorites: LiveData<Boolean>
        get() = _shouldRemoveFromFavorites

    private val _shouldAddToFavorites = MutableLiveData<Boolean>()
    val shouldAddToFavorites: LiveData<Boolean>
        get() = _shouldAddToFavorites

    fun setIsFavoriteCharacter(isFavorite: Boolean) {
        _isFavorite.value = isFavorite
    }

    fun toggleFavorite() {
        if (_isFavorite.value == true) {
            _shouldRemoveFromFavorites.postValue(true)
        } else {
            _shouldAddToFavorites.postValue(true)
        }
    }

    private val _showMessageError = MutableLiveData<Boolean>()
    val showMessageError: LiveData<Boolean>
        get() = _showMessageError

    private val _showMessageLoading = MutableLiveData<Boolean>()
    val showMessageLoading: LiveData<Boolean>
        get() = _showMessageLoading

    private val _characterInfo = MutableLiveData<CharacterResponse>()
    val characterInfo: LiveData<CharacterResponse>
        get() = _characterInfo

    fun loadCharacter() {
        _showMessageLoading.postValue(true)
        charactersService
            .getCharacterInfo(id)
            .enqueue(object : Callback<CharacterResponse> {
                override fun onResponse(
                    call: Call<CharacterResponse>?,
                    response: Response<CharacterResponse>?
                ) {
                    _showMessageLoading.postValue(false)
                    response?.run {
                        _showMessageError.postValue(response.isSuccessful.not())
                        if (isSuccessful) body()?.let { _characterInfo.postValue(it) }
                    }
                }

                override fun onFailure(call: Call<CharacterResponse>?, t: Throwable?) {
                    _showMessageLoading.postValue(false)
                    _showMessageError.postValue(true)
                }
            })
    }

}
