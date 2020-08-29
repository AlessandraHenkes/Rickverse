package com.example.rickverse.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rickverse.R
import com.example.rickverse.model.Status
import com.example.rickverse.search.result.intentSearchResultsActivity
import com.example.rickverse.util.extension.enabledChildren
import com.example.rickverse.util.extension.hideError
import com.example.rickverse.util.extension.showError
import com.example.rickverse.util.viewmodelfactory.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        ).get(SearchViewModel::class.java)

        setUI()
        initiateObserves()
    }

    private fun setUI() {
        btnSearch.setOnClickListener {
            startActivity(
                requireContext().intentSearchResultsActivity(
                    name = tieName.text?.toString(),
                    species = tieSpecies.text?.toString(),
                    status = mapStatus()
                )
            )
        }

        with(rgStatus) {
            enabledChildren(false)
            setOnCheckedChangeListener { _, _ ->
                searchViewModel.verifyStatus(checkedRadioButtonId)
            }
        }

        swStatus.setOnCheckedChangeListener { _, isChecked ->
            rgStatus.enabledChildren(isChecked)
            if (isChecked.not()) {
                rgStatus.clearCheck()
            }
        }

        verifyEditTextValue(
            editText = tieName,
            textInputLayout = tilName,
            validation = searchViewModel::verifyName
        )

        verifyEditTextValue(
            editText = tieSpecies,
            textInputLayout = tilSpecies,
            validation = searchViewModel::verifySpecies
        )
    }

    private fun initiateObserves() {
        searchViewModel.run {
            isButtonEnabled.observe(viewLifecycleOwner, Observer { isEnabled ->
                btnSearch.isEnabled = isEnabled
            })

            observeToShowError(showErrorName, tilName)
            observeToShowError(showErrorSpecies, tilSpecies)
        }
    }

    private fun verifyEditTextValue(
        editText: EditText,
        textInputLayout: TextInputLayout,
        validation: ((value: String, lostFocus: Boolean) -> Unit)
    ) {
        editText.doAfterTextChanged {
            validation(it.toString(), false)
        }
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.hideError()
            } else {
                validation(editText.text.toString(), true)
            }
        }
    }

    private fun observeToShowError(liveData: LiveData<Boolean>, textInputLayout: TextInputLayout) {
        liveData.observe(viewLifecycleOwner, Observer { shouldShow ->
            if (shouldShow) {
                textInputLayout.showError(getString(R.string.minimum_character_search_error))
            }
        })
    }

    private fun mapStatus(): String? =
        when (rgStatus.checkedRadioButtonId) {
            rbAlive.id -> Status.ALIVE
            rbDead.id -> Status.DEAD
            rbUnknown.id -> Status.UNKNOWN
            else -> null
        }?.value

}
