package com.example.breakingbad.presenters

import com.example.breakingbad.data.models.Character
import com.example.breakingbad.data.network.Response
import com.example.breakingbad.domain.GetCharactersUseCase
import com.example.breakingbad.ui.main.MainContract
import com.example.breakingbad.ui.main.MainPresenter
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


class CharactersRepositoryTest {

    lateinit var presenter: MainPresenter
    var getCharactersUseCase: GetCharactersUseCase = mockk()
    var view: MainContract.View = mockk(relaxed = true)

    companion object {
        val mockedList = arrayListOf(
            Character(charId = 0, name = "walter", appearance = listOf(1,2,3,4,5)),
            Character(charId = 1, name = "walter jr", appearance = listOf(1,2,3,4,5)),
            Character(charId = 2, name = "hey", appearance = listOf(1,2)),
            Character(charId = 3, name = "this", appearance = listOf(4,5)),
            Character(charId = 4, name = "are", appearance = listOf(1,5)),
            Character(charId = 5, name = "testing", appearance = listOf(4,5)),
            Character(charId = 6, name = "names", appearance = listOf(1)),
            Character(charId = 7, name = "in", appearance = listOf(4,5)),
            Character(charId = 8, name = "here", appearance = listOf(3,4,5)),
            Character(charId = 9, name = "ok", appearance = listOf(1)),
        )
    }


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        presenter = MainPresenter(getCharactersUseCase)
        presenter.takeView(view)

    }

    @Test
    fun loadCharacters_Failure_WillShowFailInView() {
        coEvery { getCharactersUseCase.execute() }  returns Response.Failure()

        presenter.loadCharacters()

        verify(exactly = 1) { view.showLoadingBar(true) }
        verify(exactly = 1) { view.showLoadingBar(false) }
        verify(exactly = 1) { view.showErrorOnCharactersList() }
    }

    @Test
    fun loadCharacters_SuccessEmpty_WillShowEmptyWarninfg() {
        coEvery { getCharactersUseCase.execute() }  returns Response.Success(arrayListOf())

        presenter.loadCharacters()

        verify(exactly = 1) { view.showLoadingBar(true) }
        verify(exactly = 1) { view.showLoadingBar(false) }
        verify(exactly = 1) { view.showEmptyList() }
    }

    @Test
    fun loadCharacters_SuccessList_WillShowList() {
        coEvery { getCharactersUseCase.execute() }  returns Response.Success(mockedList)

        presenter.loadCharacters()

        verify(exactly = 1) { view.showLoadingBar(true) }
        verify(exactly = 1) { view.showLoadingBar(false) }
        verify(exactly = 1) { view.showCharactersList(mockedList) }
    }

    @Test
    fun loadCharacter_Success_WillShowDetails() {
        coEvery { getCharactersUseCase.execute(any(), any(), any()) }  returns Response.Success(mockedList)

        presenter.loadCharacter(0)

        verify(exactly = 0) { view.showLoadingBar(true) }
        verify(exactly = 0) { view.showLoadingBar(false) }
        verify(exactly = 1) { view.showCharacterDetails(mockedList.first()) }
    }

    @Test
    fun loadCharacter_Failure_WillDoNothing() {
        coEvery { getCharactersUseCase.execute(any(), any(), any()) }  returns Response.Failure()

        presenter.loadCharacter(0)

        verify(exactly = 0) { view.showLoadingBar(true) }
        verify(exactly = 0) { view.showLoadingBar(false) }
        verify(exactly = 0) { view.showCharacterDetails(any()) }
    }

    @After
    fun afterTests() {
        unmockkAll()

        Dispatchers.resetMain()
    }
}