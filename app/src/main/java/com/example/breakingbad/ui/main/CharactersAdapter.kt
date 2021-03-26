package com.example.breakingbad.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.breakingbad.R
import com.example.breakingbad.data.models.Character

class CharactersAdapter(
    private var characters: ArrayList<Character>,
    private var listener: CharactersListener
) : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_character, parent, false))


    override fun onBindViewHolder(holder: CharactersAdapter.ViewHolder, position: Int) {
        val c = characters[position]
        with(holder) {
            name.text = c.name
            Glide.with(itemView.context)
                .load(c.img)
                .centerCrop()
                .into(image)
            container.setOnClickListener {
                listener.onCharacterSelected(c.charId)
            }
        }

    }

    override fun getItemCount(): Int {
        return characters.size
    }

    fun setData(listCharacters: ArrayList<Character>) {
        this.characters.clear()
        this.characters.addAll(listCharacters)
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.nameTV)
        val image = view.findViewById<ImageView>(R.id.picIV)
        val container = view.findViewById<ConstraintLayout>(R.id.container)
    }

}

interface CharactersListener {
    fun onCharacterSelected(id: Int?)
}