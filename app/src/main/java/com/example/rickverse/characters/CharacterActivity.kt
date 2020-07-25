package com.example.rickverse.characters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rickverse.R

class CharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        setUI()
    }

    private fun setUI() {}

}