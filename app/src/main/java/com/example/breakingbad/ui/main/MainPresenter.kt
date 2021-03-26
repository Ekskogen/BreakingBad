package com.example.breakingbad.ui.main

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.Response
import com.example.breakingbad.domain.GetCharactersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainPresenter @Inject constructor(
    val getCharactersUseCase: GetCharactersUseCase
): MainContract.Presenter {

    override val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun loadCharacters(season: Int?, name: String?) {
        view?.showLoadingBar(true)
        launch {
            val response = getCharactersUseCase.execute(season, name)
            withContext(Dispatchers.Main) {
                view?.showLoadingBar(false)
                if(response is Response.Failure)
                    view?.showErrorOnCharactersList()
                else if(response is Response.Success) {
                    if(response.result.isNullOrEmpty()) view?.showEmptyList()
                    else view?.showCharactersList(response.result)
                }
            }
        }
    }

    override fun loadCharacter(id: Int) {
        launch {
            val response = getCharactersUseCase.execute(id = id)
            withContext(Dispatchers.Main) {
                if (response is Response.Success && response.result.isNotEmpty())
                    view?.showCharacterDetails(response.result.first())
            }
        }
    }

    override var view: MainContract.View? = null


    override fun takeView(v: MainContract.View) {
        view = v
    }

    override fun dropView() {
        view = null
        coroutineContext.cancel()
    }
}