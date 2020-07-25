package com.example.rickverse.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.rickverse.R
import com.example.rickverse.extension.enabledChildren
import com.example.rickverse.extension.hideError
import com.example.rickverse.extension.showError
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_search.*

private const val MINIMUM_CHARACTERS_SEARCH = 3

class SearchFragment : Fragment() {

    private var isNameValid = false
    private var isSpeciesValid = false
    private var isStatusValid = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(btnSearch) {
            isEnabled = false
            setOnClickListener {
                Toast.makeText(context, "Vai buscar!", Toast.LENGTH_LONG).show()
            }
        }

        with(rgStatus) {
            enabledChildren(false)
            setOnCheckedChangeListener { _, _ ->
                isStatusValid = checkedRadioButtonId != -1
                updateButtonState()
            }
        }

        swStatus.setOnCheckedChangeListener { _, isChecked ->
            rgStatus.enabledChildren(isChecked)
            if (isChecked.not()) {
                rgStatus.clearCheck()
            }
        }

        verifyEditTextValue(editText = tieName, textInputLayout = tilName) { value ->
            isNameValid = value.length >= MINIMUM_CHARACTERS_SEARCH
            isNameValid
        }

        verifyEditTextValue(editText = tieSpecies, textInputLayout = tilSpecies) { value ->
            isSpeciesValid = value.length >= MINIMUM_CHARACTERS_SEARCH
            isSpeciesValid
        }
    }

    private fun verifyEditTextValue(
        editText: EditText,
        textInputLayout: TextInputLayout,
        validation: ((value: String) -> Boolean)
    ) {
        editText.doAfterTextChanged {
            verifyValueAndSetError(
                value = it.toString(),
                textInputLayout = textInputLayout,
                validation = validation
            )
        }
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.hideError()
            } else {
                verifyValueAndSetError(
                    value = editText.text.toString(),
                    textInputLayout = textInputLayout,
                    validation = validation,
                    showError = true
                )
            }
        }
    }

    private fun verifyValueAndSetError(
        value: String,
        textInputLayout: TextInputLayout,
        validation: ((value: String) -> Boolean),
        showError: Boolean = false
    ) {
        if (value.isNotEmpty() && validation(value).not() && showError) {
            textInputLayout.showError(getString(R.string.minimum_character_search_error))
        }

        updateButtonState()
    }


    private fun updateButtonState() {
        btnSearch.isEnabled = isNameValid || isSpeciesValid || isStatusValid
    }

}