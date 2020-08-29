package com.example.rickverse.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val MINIMUM_CHARACTERS_SEARCH = 3

class SearchViewModel : ViewModel() {

    private val _isNameValid = MutableLiveData<Boolean>()
    private val _isSpeciesValid = MutableLiveData<Boolean>()
    private val _isStatusValid = MutableLiveData<Boolean>()

    private val _showErrorName = MutableLiveData<Boolean>()
    val showErrorName: LiveData<Boolean>
        get() = _showErrorName

    private val _showErrorSpecies = MutableLiveData<Boolean>()
    val showErrorSpecies: LiveData<Boolean>
        get() = _showErrorSpecies

    private val _isButtonEnabled = MediatorLiveData<Boolean>().apply {
        addSource(_isNameValid) { updateButtonState() }
        addSource(_isSpeciesValid) { updateButtonState() }
        addSource(_isStatusValid) { updateButtonState() }
        postValue(false)
    }
    val isButtonEnabled: LiveData<Boolean>
        get() = _isButtonEnabled

    fun verifyStatus(checkedId: Int) {
        _isStatusValid.postValue(checkedId != -1)
    }

    fun verifyName(name: String, lostFocus: Boolean = false) {
        val isValid = name.length >= MINIMUM_CHARACTERS_SEARCH
        _isNameValid.postValue(isValid)
        _showErrorName.postValue(name.isNotEmpty() && isValid.not() && lostFocus)
    }

    fun verifySpecies(species: String, lostFocus: Boolean = false) {
        val isValid = species.length >= MINIMUM_CHARACTERS_SEARCH
        _isSpeciesValid.postValue(isValid)
        _showErrorSpecies.postValue(species.isNotEmpty() && isValid.not() && lostFocus)
    }

    private fun updateButtonState() {
        _isButtonEnabled.postValue(
            _isNameValid.value ?: false
                    || _isSpeciesValid.value ?: false
                    || _isStatusValid.value ?: false
        )
    }

}
