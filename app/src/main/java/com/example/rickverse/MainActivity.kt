package com.example.rickverse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rickverse.character.CharactersFragment
import com.example.rickverse.favorite.FavoritesFragment
import com.example.rickverse.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val charactersFragment by lazy { CharactersFragment() }
    private val favoritesFragment by lazy { FavoritesFragment() }
    private val searchFragment by lazy { SearchFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bnvCharacters.setOnNavigationItemSelectedListener { menuItem ->
            goTo(
                when (menuItem.itemId) {
                    R.id.page_characters -> charactersFragment
                    R.id.page_favorites -> favoritesFragment
                    R.id.page_search -> searchFragment
                    else -> charactersFragment
                }
            )
            true
        }
        bnvCharacters.selectedItemId = R.id.page_characters
    }

    private fun goTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(R.id.flFragmentContainer, fragment)
            commit()
        }
    }

}
