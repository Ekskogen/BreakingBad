package com.example.breakingbad.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.breakingbad.R
import com.example.breakingbad.data.models.Character
import kotlinx.android.synthetic.main.fragment_details.*

private const val CHARACTER = "param1"

class DetailsFragment : DialogFragment() {
    private var character: Character? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            character = it.getParcelable(CHARACTER)
        }
        if(character == null)
            this.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        character?.let { showCharacter(it) }
    }

    private fun showCharacter(c: Character) {
        context?.let {
            nameTV.text = c.name
            occupationTV.text = c.occupation.toString().replace(Regex("[\\[\\]]"),"")
            statusTV.text = c.status
            seasonsTV.text = getString(R.string.label_seasons,
                c.appearance.toString().replace(Regex("[\\[\\]]"),""))
            nickNameTV.text = c.nickname
            Glide.with(it)
                .load(c.img)
                .centerCrop()
                .into(picIV)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    companion object {
        fun newInstance(character: Character) =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHARACTER, character)
                }
            }
    }
}