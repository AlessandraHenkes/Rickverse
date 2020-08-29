package com.example.rickverse.search.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rickverse.R
import com.example.rickverse.character.CHARACTER_ID
import com.example.rickverse.character.CharacterActivity
import com.example.rickverse.character.adpter.CharactersAdapter
import com.example.rickverse.extension.showToast
import com.example.rickverse.service.RetrofitClient
import com.example.rickverse.util.CharacterViewModelFactory
import com.example.rickverse.util.EndlessScrollView
import com.example.rickverse.util.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_search_results.*
import kotlinx.android.synthetic.main.toolbar.*

private const val SEARCH_FILTER_NAME = "searchFilterName"
private const val SEARCH_FILTER_SPECIES = "searchFilterSpecies"
private const val SEARCH_FILTER_STATUS = "searchFilterStatus"

fun Context.intentSearchResultsActivity(name: String?, species: String?, status: String?) =
    Intent(
        this,
        SearchResultsActivity::class.java
    ).apply {
        putExtra(SEARCH_FILTER_NAME, name)
        putExtra(SEARCH_FILTER_SPECIES, species)
        putExtra(SEARCH_FILTER_STATUS, status)
    }

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var searchResultsViewModel: SearchResultsViewModel
    private lateinit var charactersAdapter: CharactersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        searchResultsViewModel = ViewModelProvider(
            this,
            CharacterViewModelFactory(RetrofitClient.getCharacterService())
        ).get(SearchResultsViewModel::class.java)

        setUI()
        initiateObserves()

        if (savedInstanceState == null) {
            searchCharacters()
        }
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

        rvSearchResults.run {
            addItemDecoration(
                GridSpacingItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.margin_inside)
                )
            )
            adapter = CharactersAdapter(
                mutableListOf(),
                this@SearchResultsActivity::onClickCharacter
            ).also { charactersAdapter = it }
        }

        nsvSearchResults.setOnScrollChangeListener(object : EndlessScrollView() {
            override fun onLoadMore(page: Int) {
                searchResultsViewModel.search()
            }
        })

    }

    private fun initiateObserves() {
        with(searchResultsViewModel) {
            characters.observe(this@SearchResultsActivity, Observer { characters ->
                charactersAdapter.addItems(characters.toMutableList())
            })

            observeToShowToast(showMessageHasReachedEnd, R.string.no_more_characters_available)
            observeToShowToast(showMessageLoading, R.string.loading)
            observeToShowToast(showMessageError, R.string.something_went_wrong)
        }
    }

    private fun searchCharacters() {
        searchResultsViewModel.run {
            intent?.run {
                val name = getStringExtra(SEARCH_FILTER_NAME)
                val species = getStringExtra(SEARCH_FILTER_SPECIES)
                val status = getStringExtra(SEARCH_FILTER_STATUS)
                loadFilters(name, species, status)
            }
            search()
        }
    }

    private fun observeToShowToast(liveData: LiveData<Boolean>, messageId: Int) {
        liveData.observe(this, Observer { shouldShow ->
            if (shouldShow) showToast(messageId)
        })
    }

    private fun onClickCharacter(id: Int) {
        startActivity(Intent(this, CharacterActivity::class.java).apply {
            putExtra(CHARACTER_ID, id)
        })
    }

}
