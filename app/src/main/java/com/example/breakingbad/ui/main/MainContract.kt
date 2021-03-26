package com.example.breakingbad.ui.main

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.ui.utils.BasePresenter
import com.example.breakingbad.ui.utils.BaseView

interface MainContract {

    interface View: BaseView<Presenter> {
        fun showCharactersList(characters: ArrayList<Character>)
        fun showLoadingBar(show: Boolean)
        fun showErrorOnCharactersList()
        fun showEmptyList()
        fun showCharacterDetails(character: Character)
    }

    interface Presenter: BasePresenter<View> {
        fun loadCharacters(season: Int? = null, name: String? = null)
        fun loadCharacter(id: Int)
    }
}