package com.example.rickverse.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rickverse.R
import com.example.rickverse.character.CHARACTER_ID
import com.example.rickverse.character.CharacterActivity
import com.example.rickverse.character.adapter.CharactersAdapter
import com.example.rickverse.service.RetrofitClient
import com.example.rickverse.util.GridSpacingItemDecoration
import com.example.rickverse.util.extension.showToast
import com.example.rickverse.util.viewmodelfactory.CharacterViewModelFactory
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel = ViewModelProvider(
            this,
            CharacterViewModelFactory(RetrofitClient.getCharacterService())
        ).get(FavoritesViewModel::class.java)
        setUI()
        initiateObserves()
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun setUI() {
        rvFavoritesCharacters.addItemDecoration(
            GridSpacingItemDecoration(
                resources.getDimensionPixelSize(R.dimen.margin_inside)
            )
        )
    }

    private fun initiateObserves() {
        with(favoritesViewModel) {
            characters.observe(viewLifecycleOwner, Observer { characters ->
                rvFavoritesCharacters.adapter = CharactersAdapter(
                    characters.toMutableList(),
                    this@FavoritesFragment::onClickCharacter
                )
            })

            observeToShowToast(showMessageError, R.string.something_went_wrong)
            observeToShowToast(showMessageLoading, R.string.loading)
            observeToShowToast(showMessageNoFavorites, R.string.no_favorite_characters_added)
        }
    }

    private fun loadFavorites() {
        favoritesViewModel.run {
            setFavorites(FavoritesSharedPreferencesService.getAll(requireContext()))
            loadFavorites()
        }
    }

    private fun onClickCharacter(id: Int) {
        startActivity(Intent(requireContext(), CharacterActivity::class.java).apply {
            putExtra(CHARACTER_ID, id)
        })
    }

    private fun observeToShowToast(liveData: LiveData<Boolean>, messageId: Int) {
        liveData.observe(viewLifecycleOwner, Observer { shouldShow ->
            if (shouldShow) activity?.showToast(messageId)
        })
    }

}
