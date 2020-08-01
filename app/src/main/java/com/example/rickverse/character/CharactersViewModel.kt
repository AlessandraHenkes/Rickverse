package com.example.rickverse.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickverse.model.CharacterPreview
import com.example.rickverse.model.CharactersResponse
import com.example.rickverse.service.CharactersService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersViewModel(private val charactersService: CharactersService) : ViewModel() {

    private var currentPage = 0

    private val _hasReachedEnd = MutableLiveData<Boolean>()
    val showMessageHasReachedEnd: LiveData<Boolean>
        get() = _hasReachedEnd

    private val _showMessageLoading = MutableLiveData<Boolean>()
    val showMessageLoading: LiveData<Boolean>
        get() = _showMessageLoading

    private val _showMessageError = MutableLiveData<Boolean>()
    val showMessageError: LiveData<Boolean>
        get() = _showMessageError

    private val _characters = MutableLiveData<List<CharacterPreview>>()
    val characters: LiveData<List<CharacterPreview>>
        get() = _characters

    fun loadCharacters() {
        if (_hasReachedEnd.value == true) return

        currentPage++

        _showMessageLoading.postValue(true)
        charactersService.getAll(currentPage)
            .enqueue(object : Callback<CharactersResponse> {
                override fun onResponse(
                    call: Call<CharactersResponse>?,
                    response: Response<CharactersResponse>?
                ) {
                    _showMessageLoading.postValue(false)
                    _showMessageError.postValue(response?.isSuccessful?.not())
                    response?.takeIf { it.isSuccessful }?.run {
                        body()?.run {
                            _hasReachedEnd.postValue(info.next.isNullOrEmpty())
                            _characters.postValue(characters)
                        }
                    }
                }

                override fun onFailure(call: Call<CharactersResponse>?, t: Throwable?) {
                    _showMessageLoading.postValue(false)
                    _showMessageError.postValue(true)
                }
            })
    }

}
