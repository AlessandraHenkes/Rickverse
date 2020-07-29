package com.example.rickverse.character

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rickverse.R
import com.example.rickverse.character.adpter.CharactersAdapter
import com.example.rickverse.model.CharacterPreview
import com.example.rickverse.util.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_characters.*

class CharactersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_characters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        getCharacters()
    }

    private fun setUI() {
        with(rvCharacter) {
            adapter = CharactersAdapter(
                mutableListOf(
                    CharacterPreview(
                        id = 1,
                        name = "Teste",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    ),
                    CharacterPreview(
                        id = 2,
                        name = "Teste 2",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    ),
                    CharacterPreview(
                        id = 3,
                        name = "Teste 3",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                    )
                ),
                this@CharactersFragment::onClick
            )

            addItemDecoration(
                GridSpacingItemDecoration(
                    resources.getDimensionPixelSize(
                        R.dimen.margin_inside
                    )
                )
            )

        }
    }

    private fun getCharacters(page: Int? = null) {
    }

    private fun onClick(id: Int) {
        startActivity(Intent(requireContext(), CharacterActivity::class.java).apply {
            putExtra(CHARACTER_ID, id)
        })
    }

}