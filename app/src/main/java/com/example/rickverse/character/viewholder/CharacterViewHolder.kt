package com.example.rickverse.character.viewholder

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterViewHolder(
    private val view: View,
    private val onClick: (id: Int) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun populate(imageUrl: String, id: Int, name: String) {
        with(view) {
            ivCharacter.contentDescription = name

            Glide.with(context)
                .load(imageUrl)
                .placeholder(ColorDrawable(Color.GRAY))
                .error(ColorDrawable(Color.RED))
                .into(ivCharacter)

            setOnClickListener {
                onClick(id)
            }
        }
    }

}