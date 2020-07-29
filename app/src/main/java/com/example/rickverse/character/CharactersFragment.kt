package com.example.rickverse.character

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rickverse.R
import com.example.rickverse.character.adpter.CharactersAdapter
import com.example.rickverse.extension.showToast
import com.example.rickverse.model.CharactersResponse
import com.example.rickverse.service.RetrofitClient
import com.example.rickverse.util.EndlessScrollView
import com.example.rickverse.util.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_characters.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment() {

    private lateinit var charactersAdapter: CharactersAdapter
    private var hasNextPage: Boolean = true

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
            charactersAdapter = CharactersAdapter(
                mutableListOf(),
                this@CharactersFragment::onClick
            )

            adapter = charactersAdapter

            addItemDecoration(
                GridSpacingItemDecoration(
                    resources.getDimensionPixelSize(
                        R.dimen.margin_inside
                    )
                )
            )
        }

        nsvCharacters.setOnScrollChangeListener(object : EndlessScrollView() {
            override fun onLoadMore(page: Int) {
                getCharacters(page)
            }
        })

    }

    private fun getCharacters(page: Int? = null) {
        if (hasNextPage.not()) {
            activity?.showToast(R.string.no_more_characters_available)
            return
        }

        activity?.showToast(messageId = R.string.loading)
        RetrofitClient
            .getCharacterService()
            .getAll(page)
            .enqueue(object : Callback<CharactersResponse> {
                override fun onResponse(
                    call: Call<CharactersResponse>?,
                    response: Response<CharactersResponse>?
                ) {
                    response?.takeIf { it.isSuccessful }?.run {
                        body()?.run {
                            hasNextPage = info.next.isNullOrEmpty().not()
                            charactersAdapter.addItems(characters)
                        }
                    } ?: activity?.showToast()
                }

                override fun onFailure(call: Call<CharactersResponse>?, t: Throwable?) {
                    activity?.showToast()
                }
            })
    }

    private fun onClick(id: Int) {
        startActivity(Intent(requireContext(), CharacterActivity::class.java).apply {
            putExtra(CHARACTER_ID, id)
        })
    }

}