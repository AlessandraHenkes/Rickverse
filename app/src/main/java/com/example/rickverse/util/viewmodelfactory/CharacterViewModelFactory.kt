package com.example.rickverse.util.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickverse.service.CharactersService

class CharacterViewModelFactory(
    private val charactersService: CharactersService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CharactersService::class.java)
            .newInstance(charactersService)
    }

}
