package com.example.rickverse.character.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickverse.R
import com.example.rickverse.character.viewholder.CharacterViewHolder
import com.example.rickverse.model.CharacterPreview

class CharactersAdapter(
    private val characters: MutableList<CharacterPreview>,
    private val onClick: ((id: Int) -> Unit)
) : RecyclerView.Adapter<CharacterViewHolder>() {

    private var lastPosition = characters.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_character, parent, false)

        return CharacterViewHolder(view, onClick)
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        characters[position].run {
            holder.populate(id = id, imageUrl = image, name = name)
        }
    }

    fun addItems(charactersToAdd: List<CharacterPreview>) {
        characters.addAll(charactersToAdd)
        notifyItemRangeInserted(lastPosition, charactersToAdd.size)
        lastPosition = characters.size
    }

}
